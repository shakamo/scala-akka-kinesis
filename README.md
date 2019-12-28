# scala-akka-kinesis


### JDK について
- JDK COMPATIBILITY
  - https://docs.scala-lang.org/overviews/jdk-compatibility/overview.html
- Amazon Corretto
  - https://aws.amazon.com/jp/corretto/



### 環境構築

1. JDK のインストール
```
brew tap homebrew/cask-versions
brew cask install corretto8

/usr/libexec/java_home -V

'export JAVA_HOME=`/usr/libexec/java_home -v "1.8"`' >> ~/.zshrc 
'PATH=${JAVA_HOME}/bin:${PATH}' >> ~/.zshrc 
source
```

2. Scala ビルドツールのインストール
```
brew install sbt
```

3. VS Code Plugin
```
Scala (Metals)
```


### AWS
1. aws configure --profile 'kinesis'
2. aws kinesis describe-stream --stream-name 'akka-stream' --no-include-email --profile 'kinesis'


### Kinesis Data Generator
- Webサイト
  - https://awslabs.github.io/amazon-kinesis-data-generator/web/producer.html
- CFn を実行
  - KinesisDataGeneratorCreateAccount.json 
  - オレゴンにアカウントが作成された


### サンプル
1. sbt new 
```
sbt new sbt/scala-seed.g8 
sbt new akka/akka-quickstart-scala.g8
```

### 作ろうとしているもの
![あ](https://raw.githubusercontent.com/shakamo/scala-akka-kinesis/drawio/kinesis-akka-stream.png)

### 参考
- JDK 切り替え
  - https://qiita.com/seijikohara/items/56cc4ac83ef9d686fab2
- サイト
  - https://note.com/trackiss/n/nb6ed282ccee0
- AWS にアクセスするためのライブラリについて
  - https://qiita.com/neilli-sable/items/3696c4c5f4cd3a4a23e6
- Scala：aws-java-sdkを使用してs3にファイルをアップロードする
  - https://qiita.com/ytayta/items/72665bea37187c996c5f


### AWS その他コマンド
- aws iam list-access-keys  --profile 'kinesis'


