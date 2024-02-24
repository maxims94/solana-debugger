# Solana Debugger

<!-- Plugin description -->
A RustRover plugin that allows you to debug Solana Programs.
<!-- Plugin description end --># Installation Guide

## Installation

### Prepare the environment

Install [RustRover](https://www.jetbrains.com/rust/nextversion/). The version should be at least 2023.3. Run `RUSTROVER_DIR/bin/rustrover.sh` to verify that it works.

Install Rust via rustup as described [here](https://www.rust-lang.org/tools/install).

Install the Solana Tool Suite as described in the [official documentation](https://docs.solanalabs.com/cli/install). Don't forget to update your PATH!

Download `Solana.Debugger-0.1.1.zip` from [Releases](https://github.com/maxims94/solana-debugger/releases/tag/v0.1.1)

We need to trigger the download of Solana's `platform-tools` -- since it includes the debugger client our plugin will use. To do this, first create a new Cargo project like this: `cargo new myproject`. Then `cd` into it and run `cargo-build-sbf`. This will download and install `platform-tools`. You can now remove the Cargo project: `rm -rf myproject`

The next step is to verify that `solana-lldb` (the debugger client) works: Go to `~/.local/share/solana/install/active_release/bin/sdk/sbf/dependencies/platform-tools/llvm/bin/`. Now, run `./solana-lldb`. If it starts, you can proceed to the next step. If not, you are probably missing some shared libraries. See the [Troubleshooting]() section for more information.

### Plugin installation

Download the newest ZIP-archive of the plugin from the [Releases](https://github.com/maxims94/solana-debugger/releases/tag/v0.1.1) page.

To install the plugin RustRover:

- Click on the gear icon in the top right
- Select "Plugins"
- Click on the gear icon in the top center
- Select "Install Plugin from Disk"
- Select the ZIP-archive
- Close the window

After doing this, an additional tab or icon (the Solana Logo) should appear in the bottom left. Clicking on it should open a tool window and allow you to run `solana-lldb`.

This is what it should look like: ![Screenshot](/docs/screenshot.png)

## Troubleshooting

### Missing shared libraries for `solana-lldb`

When trying to run `solana-lldb`, it may report missing libraries. Note that `solana-lldb` has been compiled against Ubuntu 20.04 libraries. So, it may be that some of them are not present on your system.

The preferred solution is to install them through your system's package manager.

For example, to make it run on Ubuntu 22.04, I needed to install python3.8:
```
sudo add-apt-repository ppa:deadsnakes/ppa
sudo apt-get update
sudo apt-get install python3.8-dev
```

In case this doesn't work, download the respective Ubuntu 20.04 package manually and extract the library files to a directory where they can be found.

Here is an example in case `libncurses` and `libpanel` are missing:
- Download it from here
- Extract the archive with `ar vx PACKAGE` and `tar xf data.tar.gz`
- Move the files to `/usr/local/lib`:
```
# cp -P /lib/* /usr/lib/*/* /usr/local/lib
```
- Make sure that `/usr/local/lib` is part of your `LD_LIBRARY_PATH`
- Run `ldd solana-lldb` to verify that it the right shared libraries will be loaded
- Now, `./solana-lldb` should work!
