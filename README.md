# SendSyslog
Java 소켓으로 UDP/TCP Syslog 전송하기


## Usage

    java -jar SendSyslog.jar <tcp|tcp-multi|udp> host <port> <filename>


* tcp : TCP로 시스로그 전송, 개행문자로 row를 구분 
* tcp-multi : TCP로 시스로그 전송, 1000건당 컨넥션을 새로 맺음
* udp : UDP로 시스로그 전송
* host : 전송할 IP, host
* port : 전송할 포트
* filename : 전송에 사용할 파일

Example - TCP를 사용하여 localhost의 6514 포트로 src_log.txt 로그를 전송하는 예:

    java -jar SendSyslog.jar tcp localhost 6514 src_log.txt

     
     
