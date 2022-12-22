#include <jni.h>
#include <stdio.h>
#include <string.h>

#ifdef __cplusplus
extern "C"{
#endif

static jclass contextClass;
static jclass signatureClass;
static jclass packageNameClass;
static jclass packageInfoClass;

/**
    之前生成好的签名字符串
*/
const char *RELEASE_SIGN = "308202e4308201cc020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b30090603550406130255533020170d3231303830363136353432345a180f32303531303733303136353432345a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330820122300d06092a864886f70d01010105000382010f003082010a02820101008b2616bebb0dd79ea6e0688ce89e5ae221e3132ee9b6cc96514119557f0be3e79371e22c3c91250988a833942714ae562e8a86d6fff4290cb7b7bf718aeacd480d2cffe38c9787218e6391e562843b95dd26642b24e2106694501f0fe39186bf5fcc5c3cca91b9d86c113ffb0acf6e0e6ac9a4cda01110c5f18729d8f2f091b9fb604595a492ddcad6ae71dd190672cd8a675483563d5a734f9ec040890456ee02a32b61ccfc61811c8311b61eb90ffb15fae0db04f52c562b3713781fd772331619a4670065ed574e96da2cf47e7c4b29af30d5bbc1e271f23f3ea33b1085bb228e44d948d1f2adb0c71ee1c2652fe5b554d5e8e430c68f35b090f7d6dccbb10203010001300d06092a864886f70d010105050003820101004f1fd6247b615c2216e23eb8fe38e20282e9d5742b9485fec941fa541c97203eb60e3419fd6742d50bd2d60274d8489d1c03ab87f604aa2632aebdb2c7cc46e42f9f6dfec32155cca601fcf4abb3724068ccda637aa11c22d361afe9ec91b0d15209a9121c849aef791ceb670052e943891c34c0d380947f442ff93a93e8c6ac594d003f40ee0880dd0a0742ad1aa5c18f692b6480c3cf3baf42f5bacd8f31e811e88e98c187da52d4ed74aeaadb5f5f2c8b99c63612ce5abf4532151bcc4f3cab9b320c12b5c5e2c7fb6a69e72d6d1acdb43415dcecf9737ed124f28850d9e691cdb03a17c6c62a51fbd5c460067f3f890df085c4a849c05b061062d2aab16c";

/*
    根据context对象,获取签名字符串
*/
const char *getSignString(JNIEnv *env, jobject contextObject) {
    jmethodID getPackageManagerId = (env)->GetMethodID(contextClass, "getPackageManager",
                                                       "()Landroid/content/pm/PackageManager;");
    jmethodID getPackageNameId = (env)->GetMethodID(contextClass, "getPackageName",
                                                    "()Ljava/lang/String;");
    jmethodID signToStringId = (env)->GetMethodID(signatureClass, "toCharsString",
                                                  "()Ljava/lang/String;");
    jmethodID getPackageInfoId = (env)->GetMethodID(packageNameClass, "getPackageInfo",
                                                    "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    jobject packageManagerObject = (env)->CallObjectMethod(contextObject, getPackageManagerId);
    jstring packNameString = (jstring)(env)->CallObjectMethod(contextObject, getPackageNameId);
    jobject packageInfoObject = (env)->CallObjectMethod(packageManagerObject, getPackageInfoId,
                                                        packNameString, 64);
    jfieldID signaturefieldID = (env)->GetFieldID(packageInfoClass, "signatures",
                                                  "[Landroid/content/pm/Signature;");
    jobjectArray signatureArray = (jobjectArray)(env)->GetObjectField(packageInfoObject,
                                                                      signaturefieldID);
    jobject signatureObject = (env)->GetObjectArrayElement(signatureArray, 0);
    return (env)->GetStringUTFChars(
            (jstring)(env)->CallObjectMethod(signatureObject, signToStringId), 0);
}

extern "C" jstring
Java_com_phone_rxjava2andretrofit2_JavaGetData_nativeMethod(JNIEnv *env, jclass thiz,
                                                            jobject contextObject) {

    const char *signStrng = getSignString(env, contextObject);
    if (strcmp(signStrng, RELEASE_SIGN) == 0)//签名一致  返回合法的 api key，否则返回错误
    {
        return (env)->NewStringUTF("rxjava_and_re_ro");
    } else {
        return (env)->NewStringUTF("error");
    }
}


/**
    利用OnLoad钩子,初始化需要用到的Class类.
*/
JNIEXPORT jint

JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {

    JNIEnv *env = NULL;
    jint result = -1;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK)
        return result;

    contextClass = (jclass) env->NewGlobalRef((env)->FindClass("android/content/Context"));
    signatureClass = (jclass) env->NewGlobalRef((env)->FindClass("android/content/pm/Signature"));
    packageNameClass = (jclass) env->NewGlobalRef(
            (env)->FindClass("android/content/pm/PackageManager"));
    packageInfoClass = (jclass) env->NewGlobalRef(
            (env)->FindClass("android/content/pm/PackageInfo"));

    return JNI_VERSION_1_4;
}

#ifdef __cplusplus
}
#endif