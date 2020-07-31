# FFPicker
## 简介
FFPicker是一款简易的Android文件选择器，支持文件和文件夹两种模式。
您可以通过以下方法来启用它：

## 使用方法
Gradle:
```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'com.silver:ffpicker:1.0.3'
}
```

```java
new FFPicker()
		.withActivity(MainActivity.this)
		.withRequestCode(123)
		.withChooseFolderMode(FFPicker.Companion.getCHOOSE_FOLDER())
		.start();
```

## License

    Copyright 2020 Silver.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
