#!/bin/bash
#
# example   -i res/mathexpr.g -p weigl.test.math -c MP -d src/main/java/



java -cp lib/weigl.jar:lib/google-collect-1.0.jar:bin:lib/freemarker.jar \
    CreateTokenParser $@
  
