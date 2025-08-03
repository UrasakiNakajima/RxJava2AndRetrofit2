#!/bin/bash

# ============== 配置区 (按需修改) ==============
DEFAULT_MODULE=":library_base64_and_file"           # 默认模块 (带冒号)
DEFAULT_BUILD_TYPE="release"    # 默认构建类型
OUTPUT_DIR="outputs/aars"     # AAR 输出目录
# ===========================================

# 解析命令行参数
while [[ $# -gt 0 ]]; do
  case "$1" in
    -m|--module)
      MODULE="$2"
      shift 2
      ;;
    -t|--type)
      BUILD_TYPE="$2"
      shift 2
      ;;
    -o|--output)
      OUTPUT_DIR="$2"
      shift 2
      ;;
    -h|--help)
      show_help
      exit 0
      ;;
    *)
      echo "⚠️ 未知参数: $1"
      show_help
      exit 1
      ;;
  esac
done

# 设置默认值
MODULE="${MODULE:-$DEFAULT_MODULE}"
BUILD_TYPE="${BUILD_TYPE:-$DEFAULT_BUILD_TYPE}"
MODULE_DIR=${MODULE#:}  # 移除冒号用于路径

# 修复：兼容的构建类型大写转换
capitalize() {
  echo "$(echo ${1:0:1} | tr '[:lower:]' '[:upper:]')${1:1}"
}

# 显示帮助信息
function show_help() {
  echo "📦 Android 模块打包工具 (AAR 生成器)"
  echo "用法: sh build.sh [选项]"
  echo "选项:"
  echo "  -m, --module <名称>   模块名 (如 :app, :lib) [默认: $DEFAULT_MODULE]"
  echo "  -t, --type <类型>     构建类型 (release/debug) [默认: $DEFAULT_BUILD_TYPE]"
  echo "  -o, --output <目录>   输出目录 [默认: $OUTPUT_DIR]"
  echo "  -h, --help            显示帮助信息"
  echo ""
  echo "示例:"
  echo "  sh build.sh -m :mylibrary -t debug"
  echo "  sh build.sh --module :app --output my_aars"
}

# 验证模块是否存在
function validate_module() {
  if [ ! -d "$MODULE_DIR" ]; then
    echo "❌ 错误: 模块目录不存在 - $MODULE_DIR"
    exit 1
  fi

  if [ ! -f "$MODULE_DIR/build.gradle" ]; then
    echo "❌ 错误: 模块构建文件不存在 - $MODULE_DIR/build.gradle"
    exit 1
  fi
}

# 检查是否是 Library 模块
function is_library_module() {
  grep -q "com.android.library" "$MODULE_DIR/build.gradle"
  return $?
}

# 转换 Application 模块为 Library
function convert_to_library() {
  echo "ℹ️ 检测到 Application 模块，尝试转换为临时 Library..."

  # 备份原始文件
  cp "$MODULE_DIR/build.gradle" "$MODULE_DIR/build.gradle.bak"

  # 修改为 Library 配置 (兼容 macOS 和 Linux)
  if [[ "$OSTYPE" == "darwin"* ]]; then
    sed -i '' 's/com.android.application/com.android.library/g' "$MODULE_DIR/build.gradle"
    sed -i '' '/applicationId/d' "$MODULE_DIR/build.gradle"
  else
    sed -i 's/com.android.application/com.android.library/g' "$MODULE_DIR/build.gradle"
    sed -i '/applicationId/d' "$MODULE_DIR/build.gradle"
  fi
}

# 恢复原始配置
function restore_config() {
  if [ -f "$MODULE_DIR/build.gradle.bak" ]; then
    echo "↩️ 恢复原始模块配置..."
    mv "$MODULE_DIR/build.gradle.bak" "$MODULE_DIR/build.gradle"
  fi
}

# 主函数
function main() {
  echo "🚀 开始打包: $MODULE ($BUILD_TYPE)"
  echo "--------------------------------"

  # 验证模块
  validate_module

  # 检查模块类型
  is_library_module
  is_library=$?

  # 处理 Application 模块
  if [ $is_library -ne 0 ]; then
    convert_to_library
    TEMP_CONVERSION=true
  fi

  # 创建输出目录
  mkdir -p "$OUTPUT_DIR"

  # 执行 Gradle 构建 (修复大小写问题)
  echo "🔧 正在构建模块..."

  # 修复：兼容的构建类型大写转换
  BUILD_TYPE_CAP=$(capitalize "$BUILD_TYPE")

  ./gradlew ${MODULE}:clean ${MODULE}:assemble${BUILD_TYPE_CAP}

  # 检查构建结果
  if [ $? -ne 0 ]; then
    echo "❌ 构建失败！"
    restore_config
    exit 1
  fi

  # 查找 AAR 文件
  AAR_PATH=$(find "$MODULE_DIR/build/outputs/aar" -name "*${BUILD_TYPE}.aar" 2>/dev/null | head -1)

  if [ -z "$AAR_PATH" ]; then
    echo "⚠️ 警告: 未找到 AAR 文件，尝试在 APK 目录搜索..."
    AAR_PATH=$(find "$MODULE_DIR/build/outputs/apk" -name "*.apk" 2>/dev/null | head -1)

    if [ -n "$AAR_PATH" ]; then
      echo "ℹ️ 找到 APK 文件，但无法生成 AAR: $AAR_PATH"
    fi
    restore_config
    exit 1
  fi

  # 复制到输出目录
  FILENAME=$(basename "$AAR_PATH")
  cp "$AAR_PATH" "$OUTPUT_DIR/$FILENAME"

  # 恢复配置（如果是临时转换）
  if [ "$TEMP_CONVERSION" = true ]; then
    restore_config
  fi

  # 计算文件大小
  FILE_SIZE=$(du -h "$OUTPUT_DIR/$FILENAME" | cut -f1)

  echo "--------------------------------"
  echo "✅ 打包成功! "
  echo "📦 生成文件: $FILENAME ($FILE_SIZE)"
  echo "📁 输出目录: $(pwd)/$OUTPUT_DIR"
  echo "⏱ 耗时: ${SECONDS} 秒"
}

# 首字母大写函数
capitalize() {
  local input="$1"
  echo "$(echo ${input:0:1} | tr '[:lower:]' '[:upper:]')${input:1}"
}

# 执行主程序
main