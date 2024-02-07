# Controller Link

Seamlessly stream controller I/O data from one machine to another.

## Usage

### Client

The client will be the machine sending the I/O data.

Plug the controller into this machine, then run the program with the `--client` argument.

Do not forget to pass in a target server with the `--target xxx.xxx.xxx.xxx` argument. This can include a port (e.g. `x.x.x.x:xxxx`.)

### Server

The server will be the machine receiving the I/O data.

Run the program with the `--server` argument.

Set the port the server listens on with `--port xxxx`

You can only allow clients of the same brand to connect with `--strictHeaders`

Once a machine attempts to connect, and you approve it, you may use the controller from the remote machine as if it was
on this machine.
