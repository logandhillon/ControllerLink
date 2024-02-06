# Controller Link

Seamlessly stream controller I/O data from one machine to another.

## Usage

### Client

The client will be the machine sending the I/O data.

Plug the controller into this machine, then run the program with the `--client` argument.

Do not forget to pass in a target server with the `--target xxx.xxx.xxx.xxx` argument.

### Server

The server will be the machine receiving the I/O data.

Run the program with the `--server` argument. 

Once a machine attempts to connect, and you approve it, you may use the controller from the remote machine as if it was
on this machine.
