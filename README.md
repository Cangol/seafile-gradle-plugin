[![Download](https://api.bintray.com/packages/cangol/maven/seafile-gradle-plugin/images/download.svg) ](https://bintray.com/cangol/maven/seafile-gradle-plugin/_latestVersion)
[![Build Status](https://travis-ci.org/Cangol/seafile-gradle-plugin.svg?branch=master)](https://travis-ci.org/Cangol/seafile-gradle-plugin)


Usage
-----------
        buildscript {
            repositories {
                jcenter()
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:3.5.3'
                classpath 'mobi.cangol.plugin:seafile-gradle-plugin:1.0.0'
            }
        }

        apply plugin: 'mobi.cangol.plugin.seafile'

        uploadSeaFile{
            server '替换为你的 seafile 主机地址' //如:https://app.seafile.com
            //推荐使用api token
            token '替换为你的 seafile Token' //如:45866cda94fbc35408d3d4f9a55e08dd1f6f0216
            //也可以使用username+password
            //username '替换为你的 seafile username'
            //password '替换为你的 seafile password'
            repo '替换为你的 seafile repo的id' //如:6d9b5843-53f5-4222-a065-9ae01baff2e6
            debugDir '替换为你的 seafile debug目录' //如:/app/Debug/
            releaseDir '替换为你的 seafile release目录' //如:/app/Release/
        }

         './gradlew uploadSeaFileApkDebug

License
-----------

    Copyright 2012 Cangol

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.