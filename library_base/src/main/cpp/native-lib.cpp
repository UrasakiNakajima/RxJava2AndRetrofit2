//
// Created by ErwinNakashima on 2022/12/24.
//
#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <string>

static jclass contextClass;
static jclass signatureClass;
static jclass packageNameClass;
static jclass packageInfoClass;

/**
    之前生成好的签名字符串
*/
const char *SIGN_RELEASE = "308203b130820299a00302010202045fa239d4300d06092a864886f70d01010b0500308188310c300a06035504061303555341311c301a060355040813135374617465206f662043616c69666f726e69613112301006035504071309437570657274696e6f310e300c060355040a13056170706c65310e300c060355040b13056170706c653126302406035504030c1d72786a617661325f616e645f726574726f666974325f72656c65617365301e170d3232313232313133333232365a170d3437313231353133333232365a308188310c300a06035504061303555341311c301a060355040813135374617465206f662043616c69666f726e69613112301006035504071309437570657274696e6f310e300c060355040a13056170706c65310e300c060355040b13056170706c653126302406035504030c1d72786a617661325f616e645f726574726f666974325f72656c6561736530820122300d06092a864886f70d01010105000382010f003082010a02820101009f09581713e084950d06b016bf86063343864fe59952c335f3bb44e14ed67686749dcd372f075d2d74757c18dff9082941cbc7c83c60c3ad51d69ca503826775b2b703a6c347d70b8364b5f140cf7e1025590c8dcfa1469d2b5af242323b6c7bcddf92fb44aaa82a7c735597112964432fc16bb6dbf360f2a44d0e9e9722295b070582ea72310c674aed8ef8aa24ec06e972edafcae51d93c7370cfbc3e804fda3cc6f22ec89f98dac2ceb5607ef564fd3151091d2e0c142b984a21c61bc63b75e0bdc931dca1a9cc76f1ee326d59ef6edeb1d5dd07dd12fa32e55de3572784e8adc67388d643b310560f77e75ec944e00e6ae62d283c90c89eae5ce71747a9f0203010001a321301f301d0603551d0e041604147387f3952464b66ce5bb906d56845bc4410d8bc1300d06092a864886f70d01010b050003820101003ca9530822c2f272bcfb94dc2552045db8d4038385fbac917e08266f6f47b00ba36a735fc62da0c2d4bbe8fcfaec0d87c7c6223c257a22240f69057f954d90fda7c7dfe4daa2f3fa482aa1a35b56c1220b449115a670324408ae9f4f6dfa3af40b9c55275c27785bcfeb1337c7228ca2deac5c9e5b4fabd33e77f3fab0c18df0facfd23980a037907acd215c11a450d98789f002081379a688686b23b3aec1fbf4e3bf1db0e34daac5140e60ad412c11c1717c3befb83ca5878d1f5b199f6f4fee89591c9dbcec13a340c7aa817ab4d68b19598f57e60b08e950ba2843d5400b576511d8b4a0ac45accf92d5c82f0d9afc11bce5c2d58ae4f3f8e9da604e83c7";
const char *SIGN_DEBUG = "308202e4308201cc020101300d06092a864886f70d010105050030373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b30090603550406130255533020170d3231303830363136353432345a180f32303531303733303136353432345a30373116301406035504030c0d416e64726f69642044656275673110300e060355040a0c07416e64726f6964310b300906035504061302555330820122300d06092a864886f70d01010105000382010f003082010a02820101008b2616bebb0dd79ea6e0688ce89e5ae221e3132ee9b6cc96514119557f0be3e79371e22c3c91250988a833942714ae562e8a86d6fff4290cb7b7bf718aeacd480d2cffe38c9787218e6391e562843b95dd26642b24e2106694501f0fe39186bf5fcc5c3cca91b9d86c113ffb0acf6e0e6ac9a4cda01110c5f18729d8f2f091b9fb604595a492ddcad6ae71dd190672cd8a675483563d5a734f9ec040890456ee02a32b61ccfc61811c8311b61eb90ffb15fae0db04f52c562b3713781fd772331619a4670065ed574e96da2cf47e7c4b29af30d5bbc1e271f23f3ea33b1085bb228e44d948d1f2adb0c71ee1c2652fe5b554d5e8e430c68f35b090f7d6dccbb10203010001300d06092a864886f70d010105050003820101004f1fd6247b615c2216e23eb8fe38e20282e9d5742b9485fec941fa541c97203eb60e3419fd6742d50bd2d60274d8489d1c03ab87f604aa2632aebdb2c7cc46e42f9f6dfec32155cca601fcf4abb3724068ccda637aa11c22d361afe9ec91b0d15209a9121c849aef791ceb670052e943891c34c0d380947f442ff93a93e8c6ac594d003f40ee0880dd0a0742ad1aa5c18f692b6480c3cf3baf42f5bacd8f31e811e88e98c187da52d4ed74aeaadb5f5f2c8b99c63612ce5abf4532151bcc4f3cab9b320c12b5c5e2c7fb6a69e72d6d1acdb43415dcecf9737ed124f28850d9e691cdb03a17c6c62a51fbd5c460067f3f890df085c4a849c05b061062d2aab16c";

std::string hello = "(*^__^*) 嘻嘻……~Hello from C++ 特朗普的头发是黄色的";

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
    jstring packNameString = (jstring) (env)->CallObjectMethod(contextObject, getPackageNameId);
    jobject packageInfoObject = (env)->CallObjectMethod(packageManagerObject, getPackageInfoId,
                                                        packNameString, 64);
    jfieldID signaturefieldID = (env)->GetFieldID(packageInfoClass, "signatures",
                                                  "[Landroid/content/pm/Signature;");
    jobjectArray signatureArray = (jobjectArray) (env)->GetObjectField(packageInfoObject,
                                                                       signaturefieldID);
    jobject signatureObject = (env)->GetObjectArrayElement(signatureArray, 0);
    return (env)->GetStringUTFChars(
            (jstring) (env)->CallObjectMethod(signatureObject, signToStringId), 0);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_phone_library_1base_JavaGetData_nativeGetString(JNIEnv *env, jclass clazz,
                                                           jobject context, jboolean is_release) {
    const char *signStrng = getSignString(env, context);
    bool isRelease = is_release;
    const char *SIGN;
    if (isRelease) {
        SIGN = SIGN_RELEASE;
    } else {
        SIGN = SIGN_DEBUG;
    }
    if (strcmp(signStrng, SIGN) == 0)//签名一致  返回合法的 api key，否则返回错误
    {
        return (env)->NewStringUTF(hello.c_str());
    } else {
        return (env)->NewStringUTF("error");
    }
}

//bool toCppBool(jboolean value) {
//    return value == JNI_TRUE;
//}

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
