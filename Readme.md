# Android.mk用法详解

**一、Android.mk介绍**

Android.mk是Android提供的一种makefile文件，用来指定诸如编译生成so库名、引用的头文件目录、需要编译的.c/.cpp文件和.a静态库文件等。要掌握jni，就必须熟练掌握Android.mk的语法规范。
 

它的基本格式如下：

```
LOCAL_PATH := $(call my-dir)  
include $(CLEAR_VARS)  
 
 ................  
LOCAL_xxx       := xxx  
LOCAL_MODULE    := hello-jni  
LOCAL_SRC_FILES := hello-jni.c  
LOCAL_xxx       := xxx  
  ................  

include $(BUILD_SHARED_LIBRARY)  

```


LOCAL_PATH变量制定了该.mk的路径，

$(call my-dir)调用NDK内部的函数获得当前.mk文件的路径

include $(CLEAR_VARS)清空了除了LOCAL_PATH之外的所有LOCAL_xxx变量的值

省略号中间就是对于模块参数的设置，主要包括：模块名字、模块源文件、模块类型、编译好的模块存放位置、以及编译的平台等

include $(BUILD_xxx_xxx)执行NDK的默认脚本，它会收集include $(CLEAR_VARS)脚本后所有定义的LOCAL_xxx变量，然后根据它们来生成模块。



**二、Android.mk语法详解**

LOCAL_PATH := $(call my-dir) 
每个Android.mk文件必须以定义LOCAL_PATH为开始。它用于在开发tree中查找源文件。宏my-dir 则由Build System提供。返回包含Android.mk的目录路径。



include $(CLEAR_VARS) 
CLEAR_VARS 变量由Build System提供。并指向一个指定的GNU Makefile，由它负责清理很多LOCAL_xxx.
例如：LOCAL_MODULE, LOCAL_SRC_FILES, LOCAL_STATIC_LIBRARIES等等。但不清理LOCAL_PATH.
这个清理动作是必须的，因为所有的编译控制文件由同一个GNU Make解析和执行，其变量是全局的。所以清理后才能避免相互影响。



LOCAL_MODULE    := hello-jni 

LOCAL_MODULE模块必须定义，以表示Android.mk中的每一个模块。名字必须唯一且不包含空格。Build System会自动添加适当的前缀和后缀。例如，foo，要产生动态库，则生成libfoo.so. 但请注意：如果模块名被定为：libfoo.则生成libfoo.so. 不再加前缀



LOCAL_MODULE_PATH :=$(TARGET_ROOT_OUT) 指定最后生成的模块的目标地址

TARGET_ROOT_OUT:根文件系统，路径为out/target/product/generic/root

TARGET_OUT:system文件系统，路径为out/target/product/generic/system

TARGET_OUT_DATA:data文件系统，路径为out/target/product/generic/data

除了上面的这些，NDK还提供了很多其他的TARGET_XXX_XXX变量，用于将生成的模块拷贝到输出目录的不同路径

默认是TARGET_OUT

LOCAL_SRC_FILES := hello-jni.c 

LOCAL_SRC_FILES变量必须包含将要打包如模块的C/C++ 源码。不必列出头文件，build System 会自动帮我们找出依赖文件。缺省的C++源码的扩展名为.cpp. 也可以修改，通过LOCAL_CPP_EXTENSION



include $(BUILD_SHARED_LIBRARY) 
BUILD_SHARED_LIBRARY：是Build System提供的一个变量，指向一个GNU Makefile Script。
它负责收集自从上次调用 include $(CLEAR_VARS)  后的所有LOCAL_XXX信息。并决定编译为什么。

BUILD_STATIC_LIBRARY    ：编译为静态库。 
BUILD_SHARED_LIBRARY ：编译为动态库 
BUILD_EXECUTABLE           ：编译为Native C可执行程序  

BUILD_PREBUILT                 ：该模块已经预先编译

NDK还定义了很多其他的BUILD_XXX_XXX变量，它们用来指定模块的生成方式。

```
#编译静态库    
LOCAL_PATH := $(call my-dir)    
include $(CLEAR_VARS)    
LOCAL_MODULE = libhellos    
LOCAL_CFLAGS = $(L_CFLAGS)    
LOCAL_SRC_FILES = hellos.c    
LOCAL_C_INCLUDES = $(INCLUDES)    
LOCAL_SHARED_LIBRARIES := libcutils    
LOCAL_COPY_HEADERS_TO := libhellos    
LOCAL_COPY_HEADERS := hellos.h    
include $(BUILD_STATIC_LIBRARY)    
    
#编译动态库    
LOCAL_PATH := $(call my-dir)    
include $(CLEAR_VARS)    
LOCAL_MODULE = libhellod    
LOCAL_CFLAGS = $(L_CFLAGS)    
LOCAL_SRC_FILES = hellod.c    
LOCAL_C_INCLUDES = $(INCLUDES)    
LOCAL_SHARED_LIBRARIES := libcutils    
LOCAL_COPY_HEADERS_TO := libhellod    
LOCAL_COPY_HEADERS := hellod.h    
include $(BUILD_SHARED_LIBRARY)    
    
#使用静态库    
LOCAL_PATH := $(call my-dir)    
include $(CLEAR_VARS)    
LOCAL_MODULE := hellos    
LOCAL_STATIC_LIBRARIES := libhellos    
LOCAL_SHARED_LIBRARIES :=    
LOCAL_LDLIBS += -ldl    
LOCAL_CFLAGS := $(L_CFLAGS)    
LOCAL_SRC_FILES := mains.c    
LOCAL_C_INCLUDES := $(INCLUDES)    
include $(BUILD_EXECUTABLE)    
    
#使用动态库    
LOCAL_PATH := $(call my-dir)    
include $(CLEAR_VARS)    
LOCAL_MODULE := hellod    
LOCAL_MODULE_TAGS := debug    
LOCAL_SHARED_LIBRARIES := libc libcutils libhellod    
LOCAL_LDLIBS += -ldl    
LOCAL_CFLAGS := $(L_CFLAGS)    
LOCAL_SRC_FILES := maind.c    
LOCAL_C_INCLUDES := $(INCLUDES)    
include $(BUILD_EXECUTABLE)    
  
  
#拷贝文件到指定目录  
LOCAL_PATH := $(call my-dir)  
include $(CLEAR_VARS)  
LOCAL_MODULE := bt_vendor.conf  
LOCAL_MODULE_CLASS := ETC  
LOCAL_MODULE_PATH := $(TARGET_OUT)/etc/bluetooth  
LOCAL_MODULE_TAGS := eng  
LOCAL_SRC_FILES := $(LOCAL_MODULE)  
include $(BUILD_PREBUILT)  
  
  
#拷贝动态库到指定目录  
LOCAL_PATH := $(call my-dir)  
include $(CLEAR_VARS)  
#the data or lib you want to copy  
LOCAL_MODULE := libxxx.so  
LOCAL_MODULE_CLASS := SHARED_LIBRARIES  
LOCAL_MODULE_PATH := $(ANDROID_OUT_SHARED_LIBRARIES)  
LOCAL_SRC_FILES := lib/$(LOCAL_MODULE )  
OVERRIDE_BUILD_MODULE_PATH := $(TARGET_OUT_INTERMEDIATE_LIBRARIES)  
include $(BUILD_PREBUILT)  
```



# Application.mk用法详解



```
APP_PLATFORM = android-8
APP_ABI := armeabi-v7a
APP_STL := stlport_static
APP_OPTIM := debug
```

- APP_PLATFORM     使用的ndk库函数版本号。一般和SDK的版本相对应，各个版本在NDK目录下的platforms文件夹中
- APP_ABI        编译成什么类型的cpu的so, 拥有三个属性armeabi  armeabi-v7a  x86可以全选 也可以只用一个，如果全选也可以使用all.
- APP_STL      如何连接c++标准库 。
         stlport_static    静态链接 
         stlport_shared    动态链接 
         system    系统默认
- APP_OPTIM   编译版本，如果是DEBUG版本就会带上调试信息。可以使用gdb-server进行动态断点低调试。
         debug   调试版本    so中带调试信息，
         release  发布版本   so不带调试信息
