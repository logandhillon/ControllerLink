#!/usr/bin/expect

set timeout 20
spawn telnet localhost 4350
send "ver:logandhillon,Debug_Terminal_Client,client,1.0
"
interact