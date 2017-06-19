![](https://raw.githubusercontent.com/ExploiTR/JDoS/master/icon/e.ico)
# JDoS
ICMP Ping Flooding [POD] `Windows` Application Written In Java.

## [ABOUTOF : POD](https://en.wikipedia.org/wiki/Ping_of_death)  
A ping of death is a type of attack on a computer system that involves sending a malformed or otherwise malicious ping to a computer.  
A correctly-formed ping packet is typically `56` bytes in size, or `64` bytes when the Internet Protocol header is considered.  
However, any `IPv4` packet (including pings) may be as large as `65,535` bytes.  
Some computer systems were never designed to properly handle a ping packet larger than the maximum packet size because it violates the Internet Protocol documented in RFC 791.  
Like other large but well-formed packets, a ping of death is fragmented into groups of `8 octets` before transmission.  
However, when the target computer reassembles the malformed packet, a buffer overflow can occur, causing a system crash and potentially allowing the injection of malicious code.  
In early implementations of TCP/IP, this bug is easy to exploit and can affect a wide variety of systems including Unix, Linux, Mac, Windows, and peripheral devices.  
As systems began filtering out pings of death through firewalls and other detection methods, a different kind of ping attack known as ping flooding later appeared, which floods the victim with so many ping requests that normal traffic fails to reach the system (`a basic denial-of-service attack`).

## How JDoS Works
1. JDoS sends large 65500 (`MAX Allowed for Windows Env.`) bytes of packets for each process started by it.
2. JDoS (`My release - again`) creates a number of 100 processes.
3. So, totally it sends 65500×100 bytes/sec == 6397 kb/sec == 6.24 mb/sec to victim theoretically.  
  *Practically it was avarage 6.8mb/sec for 100 processes in my case*.

## TWEAKING :  
My ISP Internet Connection supports 100mbit/s max. So, I limited it to 100 processes.  
You can tweak it as much as you can [`If your PC is compatiable`].

## A HACKISH WORKAROUND
When I was thinking about `How can I improve JDoS..` I found that clicking the attack button multiple times(ex.5) multiplies processes(5×100). I hit the button 12 times and saw a massive *21 mbyte/s* outputstream from my Computer through my 100 *mbit/s* Internet connection.

If you are thinking me mad because why I didn't put `12×100 = 1200` processes in code ~ Then, I remember you that, If I try to open *1200* processes at once, My PC which has only 8GB RAM and 3.30 GHz Processor will not be able to take it.

But, after observing through Task manager, I came to this decision that, if you take much time to increase the traffic(process) your PC will get less slower. `(If you have less Computer Specification like me)`

