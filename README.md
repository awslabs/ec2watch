# ec2watch

ec2watch is a simple tool that can capture screenshots of an EC2 instance's console repeatedly and display them.  The tool invokes the [GetConsoleScreenshot](GetConsoleScreenshot) API so the necessary IAM permissions are required.

## Download

Download the latest release from: https://github.com/awslabs/ec2watch/releases or ![click here](https://github.com/awslabs/ec2watch/releases/download/v1.0.0/ec2watch.jar).

## Usage

```
usage: java -jar ec2watch.jar [<options>] INSTANCE_ID
 -h         Show this help
 -r <arg>   AWS Region
```

Example:  java -jar ec2watch.jar i-04e2556b3868fdb6d


## Demo

![ec2watch demo](/images/screencap.gif)