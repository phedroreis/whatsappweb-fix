# Esse script compila o projeto whatsappweb-fix e cria os JARs executáveis.
# 
# Autor: "Pedro Reis" 29 de novembro de 2020
#
#!bin/bash  

echo "*****************************************************************************"
echo "* Para compilar esse projeto eh aconselhavel que voce tenha instalado  em   *"
echo "* seu sistema o OracleJDK14 ou posterior.                                   *"
echo "*                                                                           *"
echo "* Para isso desinstale o OpenJDK com o comando:                             *"
echo "*                                                                           *"
echo "* $ sudo apt-get remove --purge openjdk-*                                   *"
echo "*                                                                           *"
echo "* E instale o OracleJDK14 com:                                              *"
echo "*                                                                           *"
echo "* $ sudo add-apt-repository ppa:linuxuprising/java                          *"
echo "* $ sudo apt update                                                         *"
echo "* $ sudo aptinstall oracle-java14-installer                                 *"
echo "*                                                                           *"
echo "* Vai surgir uma tela com o contrato de licenca da Oracle no terminal.      *"
echo "* Role ate o fim e tecle ESC. Entao selecione <sim> para aceitar e a tecla  *"
echo "* ENTER. Pronto, o OracleJDK14 esta instalado em seu sistema.               *"
echo "*                                                                           *"
echo "* Se voce jah tem o OracleJDK tecle <ENTER> para continuar, caso contrario  *"
echo "* tecle <CTRL+C> para abortar.                                              *"
echo "*                                                                           *"
echo "*****************************************************************************" 

read n  

echo " COMPILANDO PROJETO..."
echo ""

javac br/com/hkp/whatsappwebfix/*.java
javac br/com/hkp/whatsappwebfix/apps/*.java
javac br/com/hkp/whatsappwebfix/global/*.java
javac br/com/hkp/whatsappwebfix/util/*.java
javac br/com/hkp/whatsappwebfix/gui/*.java

echo ""
echo " CONSTRUINDO ARQUIVO EXECUTAVEL JAR..."
echo ""

jar cfm Normalize.jar Normalize.txt br/com/hkp/whatsappwebfix/*.class br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class  br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class

jar cfm Fix.jar Fix.txt br/com/hkp/whatsappwebfix/*.class br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class  br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class
 
jar cfm GetPngs.jar GetPngs.txt br/com/hkp/whatsappwebfix/*.class br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class

jar cfm Update.jar Update.txt br/com/hkp/whatsappwebfix/*.class br/com/hkp/whatsappwebfix/apps/*.class br/com/hkp/whatsappwebfix/global/*.class br/com/hkp/whatsappwebfix/util/*.class br/com/hkp/whatsappwebfix/gui/*.class
echo ""
echo "------ ARQUIVOS JAR  CRIADOS --------"

