# Solana Debugger

<!-- Plugin description -->
This RustRover plugin is a graphical debugger for Solana Programs.
<!-- Plugin description end -->

## Features

In the future, this plugin will allow you to debug Solana programs as though they are regular programs.

This includes common debugger features such as displaying and navigating the call stack, evaluating expressions, controlling execution (step in, over and out of subroutines) and variable inspection. All of this will be seamlessly integrated into the RustRover IDE.

It will also have Solana-specific features: There will be a dedicated UI to configure a program's input (program id, accounts, instruction data). Also, it will be possible to pre-load the ledger with a custom state (accounts and programs) before executing the program.

Possible use cases are fixing hard bugs, ensuring correct execution and learning development on Solana.

## Screenshot

![Screenshot](/docs/screenshot.png)

## Installation (macOS)

Tested with:
* macOS Sonoma 14.4
* Solana v1.18.2 (platform-tools v1.39)
* RustRover 2023.3 EAP (Build #RR-233.14015.155)
* Solana Debugger v0.1.1

Install Rust via rustup as described [here](https://www.rust-lang.org/tools/install).

Install the Solana Tool Suite as described in the [official documentation](https://docs.solanalabs.com/cli/install).

Trigger the installation of Solana's `platform-tools` (if not yet installed):
```
$ cd ~
$ cargo new myproject
$ cd myproject
$ cargo-build-sbf # This will download and install platform-tools
$ cd ..
$ rm -rf myproject
```

Verify that `solana-lldb` runs on your system:
```
$ cd ~/.local/share/solana/install/active_release/bin/sdk/sbf/dependencies/platform-tools/llvm/bin
$ ./solana-lldb
```
This should start a new debugger session. If it doesn't work, additional steps are required. See [Troubleshooting](#troubleshooting).

## Installation (Ubuntu)

Tested with:
* Ubuntu 22.04 LTS
* Solana v1.18.2 (platform-tools v1.39)
* RustRover 2023.3 EAP (Build #RR-233.14015.155)
* Solana Debugger v0.1.1

Install Rust via rustup as described [here](https://www.rust-lang.org/tools/install).

Install the Solana Tool Suite as described in the [official documentation](https://docs.solanalabs.com/cli/install).

Trigger the installation of Solana's `platform-tools` (if not yet installed):
```
$ cd ~
$ cargo new myproject
$ cd myproject
$ cargo-build-sbf # This will download and install platform-tools
$ cd ..
$ rm -rf myproject
```

Install python3.8:
```
sudo add-apt-repository ppa:deadsnakes/ppa
sudo apt-get update
sudo apt-get install python3.8-dev
```

Verify that `solana-lldb` runs on your system:
```
$ cd ~/.local/share/solana/install/active_release/bin/sdk/sbf/dependencies/platform-tools/llvm/bin
$ ./solana-lldb
```
This should start a new debugger session. If it doesn't work, additional steps are required. See [Troubleshooting](#troubleshooting).

## RustRover + Plugin installation

Install [RustRover](https://www.jetbrains.com/rust/nextversion/). The version should be at least 2023.3. Run `RUSTROVER_DIR/bin/rustrover.sh` to verify that it works.

Download `Solana.Debugger-0.1.1.zip` from [Releases](https://github.com/maxims94/solana-debugger/releases/tag/v0.1.1).

To install the plugin in RustRover:

- Click on the gear icon in the top right
- Select "Plugins" to open the Plugins window
- In this window, click on the gear icon in the middle of the top bar
- Select "Install Plugin from Disk"
- Select the ZIP archive
- Confirm the dialog
- Click on "OK" in the Plugins window

Now, an additional tab or icon (the Solana Logo) should appear in the bottom left. Clicking on it should open a tool window and allow you to run `solana-lldb` within RustRover (see screenshot above).

## Troubleshooting

### Linux: Missing shared libraries for `solana-lldb`

When trying to run `solana-lldb`, it may report missing libraries. Note that the `solana-lldb` Linux binary has been compiled against Ubuntu 20.04 libraries. So, it may be that some of them are not available on your system.

The preferred solution is to install them through your package manager.

If this doesn't work, you need to download and install the respective Ubuntu 20.04 libraries manually.

Here is an example in case `libncurses` and `libpanel` are missing:

- Download it from here: https://packages.ubuntu.com/focal/libncurses6
- Extract the archive with `ar vx DEB_PACKAGE` and `tar xf data.tar.gz`
- Copy the library files to `/usr/local/lib`:
```
# cp -P PACKAGE/lib/*/* PACKAGE/usr/lib/*/* /usr/local/lib
```
- Make sure that `/usr/local/lib` is part of your `LD_LIBRARY_PATH`
- Run `ldd solana-lldb` to verify that the shared libraries are found
- Now, `./solana-lldb` should work!
