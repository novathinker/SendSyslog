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

     
### TCP 시스로그를 전송하고 이를 로그프레소 시스로그 수집기를 통해 수집, 저장된 모습    
![image](https://user-images.githubusercontent.com/641604/39091744-d0d70712-4636-11e8-88fc-2cf38d5972fd.png)
