echo off
echo "*****************************************************************************"
echo "* Para compilar esse projeto eh aconselhavel que voce tenha instalado  em   *"
echo "* seu sistema o OracleJDK14 ou posterior.                                   *"
echo "*                                                                           *"
echo "* Tecle ENTER para compilar.                                                *"
echo "*****************************************************************************" 
pause
echo ""
echo "Compilando..."
echo ""

javac br/com/hkp/whatsappwebfix/*.java
javac br/com/hkp/whatsappwebfix/apps/*.java
javac br/com/hkp/whatsappwebfix/global/*.java
javac br/com/hkp/whatsappwebfix/util/*.java
javac br/com/hkp/whatsappwebfix/gui/*.java

echo ""
echo "Criando JARs..."
echo ""

jar cfm Normalize.jar Normalize.txt br/com/hkp/whatsappwebfix/*.class br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class  br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class

jar cfm Fix.jar Fix.txt br/com/hkp/whatsappwebfix/*.class br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class  br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class
 
jar cfm GetPngs.jar GetPngs.txt br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class

jar cfm GetJpgs.jar GetJpgs.txt br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class
