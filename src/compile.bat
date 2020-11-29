
javac br/com/hkp/whatsappwebfix/*.java
javac br/com/hkp/whatsappwebfix/apps/*.java
javac br/com/hkp/whatsappwebfix/global/*.java
javac br/com/hkp/whatsappwebfix/util/*.java
javac br/com/hkp/whatsappwebfix/gui/*.java


jar cfm Normalize.jar Normalize.txt br/com/hkp/whatsappwebfix/*.class br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class  br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class

 
jar cfm GetPngs.jar GetPngs.txt br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class
