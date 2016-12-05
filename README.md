[![Build Status](https://travis-ci.org/ygunayer/talos-decoder.svg)](https://travis-ci.org/ygunayer/talos-decoder.svg)
[![Coverage Status](https://coveralls.io/repos/ygunayer/talos-decoder/badge.svg?branch=master&service=github)](https://coveralls.io/github/ygunayer/talos-decoder?branch=master)

# The Talos Compendium
The data generation components of The Talos Compendium, a text database for [The Talos Principle](http://www.croteam.com/talosprinciple/)

For the UI and web app component of the project, see [ygunayer/talos-ui](https://github.com/ygunayer/talos-ui)

![http://i.imgur.com/FMMJJQj.png](http://i.imgur.com/FMMJJQj.png "Screenshot")

## About
For a more detailed explanation on the project, see the *About* section on [ygunayer/talos-ui/README.md](https://github.com/ygunayer/talos-ui/README.md)

## Prerequisites
- Gradle (tested w/ 2.5)
- JDK 8
- An Elastic installation

## Building and Running
To compile, test and then build a single JAR file of the project, run the following command:

```gradle distZip```

This will place a compressed file under ```build/distributions/talos-decoder.zip```. Unzip this file to a folder of your choice, and then navigate to the ```bin``` folder located inside. There, run the batch file named ```talos-encoder.bat``` or the Bash script named ```talos-encoder```. The application will need the path to the root folder of the game, and this can be provided in two ways:

### Properties File
When running the app, you can specify the path to a properties file which may contain the following properties:
- **game.path**: The path to the root folder of the game. No default value. The ```game.path``` JVM argument overrides this value.
- **game.locale**: The 3-letter code of the language to extract the data in. Default value: ```enu```. All possible values are:
  - **chs**: Chinese (Simplified)
  - **cht**: Chinese (Traditional)
  - **deu**: German
  - **enu**: English
  - **esp**: Spanish
  - **fra**: French
  - **hrv**: Croatian
  - **itl**: Italian
  - **jpn**: Japanese
  - **kor**: Korean
  - **plk**: Polish
  - **por**: Portugese
  - **rus**: Russian
- **elastic.hostname***: The hostname of the Elastic node to connect to. Default value: ```localhost```
- **elastic.port***: The port of the Elastic node to connect to. Default value: ```9200```

So, an example properties file may go like this:

```
game.path=D:\Games\Steam\common\The Talos Principle
game.locale=enu
elastic.hostname=localhost
elastic.port=9200
```

**Example**  
```
talos-encoder some-props.properties
```

### game.path VM Argument
The game path can also be set by the ```game.path``` JVM argument. If a properties file is specified, the ```game.path``` value exposed by that file will be overridden by this value.

When running the ```talos-encoder``` batch file or Bash script, this argument can be specified by setting the ```TALOS_ENCODER_OPTS``` or ```JAVA_OPTS``` environment variable to a value such as ```-Dgame.path=<some-path>```.

**Example**  
```
export TALOS_ENCODER_OPTS=-Dgame.path=/Path/To/Steam/common/The Talos Principle; bash talos-decoder
```

## Contributing
To send feedback, request a feature or to report an error, please open an issue. To contribute code-wise, feel free to fork the project and send a pull request.

## What's Missing
- Not enough tests
- Very little documentation
- Even though the code itself does not contain anything from the game itself, I'm not sure about which license to use, so there's no license yet
- I originally planned to allow users to change the input and output methods, of which the relevant classes are still located in the project, but then I decided to postpone it until later
- Both original and decoded texts as well as the file names and the key they're stored in the game assets as are all indexed in Elastic, but the search only operates on the filename field as a ```prefix``` search
- The encoded text segments are captured blindly, so some texts might have been decoded unnecessarily, incorrectly, or might not even have been decoded at all. To address this issue, the extraction process might be improved, or some manual or pre-defined post processes might be constructed to patch the data in Elastic
- Texts in different languages should be stored in different indices or index types in Elastic
