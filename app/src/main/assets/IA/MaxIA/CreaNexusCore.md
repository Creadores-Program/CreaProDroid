# CreaNexusCore
**CreaNexusCore** is an ambitious, open-source server architecture designed to unify modifiable games and custom-server environments under a single, streamlined ecosystem.


Our mission is to bridge the gap between diverse sandbox gaming communities, providing a centralized core that facilitates interoperability, cross-platform management, and modular extensibility for titles such as **Minecraft** (Java, Bedrock, NetEase, etc.), **ClassiCube**, **Luanti** (formerly Minetest), **Multicraft**, **Hytale**, **Roblox**, **Polytoria**, and more.

## 🌍 Supported Ecosystems & Status
We track the progress of each engine and protocol bridge. Contributions, bug reports, and pull requests are highly encouraged for items marked as "Experimental" or "In Development."

### ✅ Stable / Semi-Stable
These integrations are functional but may still have edge-case bugs.
- **Minecraft (Java Edition)**: Full support (Release/Beta/April Fools).
- **Minecraft (Bedrock Edition)**: Full support. (1.2-1.26)
- **Minecraft (NetEase Edition)**: Functional bridge.
- **EaglerCraft**: Fully integrated.
- **Discord Chat**: Stable connectivity.
- **Minecraft Console Edition (Legacy)**: Semi-stable support for Xbox 360, PS3, Wii U, and early PS4/Xbox One versions via VoxelBridge.
- **ClassiCube**: Stable connection (currently lacking Inventory, Xbox Auth, and Form support).
- **Luanti / Minetest**: Functional via [BridgeTest](https://codeberg.org/l-koehler/bridgetest) (Requires a Rust-compatible hosting environment; routes through ViaProxy).

### ⚠️ Experimental / W.I.P.
These implementations are currently in a testing phase or have limited functionality.
- **Minecraft PE 0.15.x**: Currently in "Spectator Mode" (Limited rendering; no entity support or terrain generation).

### 🏗️ Planned / In Development
We are currently architecting the protocol bridges for these titles.
- **Hytale**
- **Roblox**
- **Polytoria**

## ⚙️ The Core Architecture: Nukkit MOT
The heart of **CreaNexusCore** is powered by Nukkit MOT, a highly specialized fork of the Nukkit server software. This core handles the heavy lifting of multi-protocol communication, allowing us to bridge the vast gap between Bedrock versions.

### Core Capabilities:
- **Version Support**: Extensive support ranging from v1.2 to v1.26 (Bedrock).
- **NetEase Integration**: Native support for the NetEase protocol variants.
- **Experimental Legacy Support**: Preliminary support for v1.1.

## 🔗 Protocol Translation Layer: ViaProxy
To ensure cross-version compatibility for Java and Bedrock clients, we utilize **ViaProxy**. This module acts as the "universal translator," allowing legacy, modern, and experimental Minecraft clients to communicate with our core.

### Supported Protocol Versions
- **Java Edition**: Full spectrum support from Alpha (a1.0.15) to modern Releases (1.21.11).
- **Beta/Alpha**: Extensive legacy support (a1.0.15 – b1.8.1).
- **April Fools**: Specialized support for 3D Shareware, 20w14infinite, and 25w14craftmine.
- **Combat Snapshots**: Combat Test 8c.
- **Bedrock Edition**: Support for v1.26.0+.

### 🛡️ Deployment Flexibility: ViaProxyNoCommandRun
We understand that not every hosting environment allows you to modify the Java startup arguments. **ViaProxyNoCommandRun** is our solution for deploying **ViaProxy** within restrictive environments (like standard shared Minecraft hostings).
- **How it works**: This specialized .jar bypasses command-line argument requirements, enabling the proxy to initiate successfully in environments where you only have access to a "Start" button or a standard JAR selector.
- **Best for**: Shared hostings, pre-configured game panels, and quick deployment scenarios where command-line control is unavailable.

[ViaProxyNoCommandRun](https://github.com/Creadores-Program/ViaProxyNoCommandRun)

### Configuration for CreaNexusCore
ViaProxy must be linked to your **Nukkit MOT** instance (ip and port, ViaBedrock mode) to successfully bridge these versions.

## 🔗 Protocol Translation Layer: ViaProxy & Beta2Release
To achieve seamless interoperability across Minecraft's history, **CreaNexusCore** leverages **ViaProxy** as the main traffic mediator, enhanced by the **ViaProxyBeta2Release** plugin.

### The Translation Stack
This combination enables a unified experience for players across widely disparate versions:
- **ViaProxy Core**: Handles the heavy lifting of multi-protocol communication (Java Alpha to modern Release, Bedrock 1.26.0, and Combat Snapshots).
- **ViaProxyBeta2Release Plugin**: Specifically bridges the **Beta (b1.0 - b1.8.1)** protocols into the modern Release environment.

## 🌐 EaglerCraft Integration: ayunViaProxyEagUtils
We bridge the browser-based world of **EaglerCraft** (v1.5.2 and v1.8.8) directly into our server infrastructure. By utilizing the ayunViaProxyEagUtils plugin within **ViaProxy**, we enable WebSocket-to-TCP communication on the same port as your Java traffic.

### Key Features:
- **WebSocket Support**: Seamlessly handles ws:// connections.
- **Dual-Version Support**: Compatible with both 1.5.2 and 1.8.8 EaglerCraft clients.
- **Smart Passthrough**: Supports legacy passthrough and ProtocolSupport, ensuring compatibility even if the backend server does not natively support older protocols.
>  [!NOTE]
  Please be aware that custom skin files are excluded from this bridge.

## 🎮 Minecraft Console Edition (Legacy) Integration: VoxelBridge
We provide compatibility for the **Legacy Console Edition** of Minecraft (Xbox 360, PS3, Wii U, etc.) through a specialized bridging layer. This allows controllers and consoles from the previous generation to join the **CreaNexusCore** ecosystem.

### The Connection Pipeline
To ensure proper protocol translation and prevent stability issues, the connection must follow this specific route:
1. **Console Client**: The console connects to the **VoxelBridge** proxy.
2. **VoxelBridge**: Acts as the initial interpreter for the Console Edition protocol.
3. **ViaProxy**: VoxelBridge **must** be configured to point towards our **ViaProxy** instance.
4. **Nukkit MOT**: ViaProxy finally delivers the translated packets to the core.

- **Status**: Semi-Stable. 
- **Key Requirement**: It is mandatory to daisy-chain **VoxelBridge -> ViaProxy** to resolve version mismatches and ensure that console players are recognized as valid Java-protocol entities before hitting the Bedrock-based core.

## 💬 Community Bridge: Discord Integration
To maintain a unified community, **CreaNexusCore** includes a native **DiscordChat**. This plugin synchronizes your in-game chat with your Discord server, ensuring seamless communication between players on the server and players on Discord.

### Core Features
- **Two-Way Sync**: Messages sent in the game are relayed to a specified Discord channel, and messages from that channel are broadcasted in the game.
- **Bot-Driven**: Powered by a dedicated Discord bot configured within your Nukkit MOT instance.
- **Management**: Real-time visibility of server activity, player joins/leaves, and chat logs directly from your server's Discord hub.

## 🔧 ClassiCube Integration: Barrel Crea Classic
To extend CreaNexusCore into the classic sandbox era, we have integrated Barrel Crea Classic. This is a specialized fork of BarrelMC (the predecessor to ViaProxy), heavily modified to support the unique requirements of the ClassiCube protocol.

- **The Bridge Architecture**: By leveraging the logic of ViaBedrock (Geyser Reverse) and the re-architected Java-to-ClassiCube translation layer, Barrel Crea Classic allows legacy-style sandbox clients to interact with our modern server ecosystem.
- **Protocol Restructuring**: The core has been modified to restructure standard Java packets into the ClassiCube protocol format, effectively allowing seamless communication between your backend and the ClassiCube client.
- **Status**: Stable. The connection and packet stream are now highly reliable. We have successfully resolved previous byte-stream errors, establishing a consistent gameplay experience. 
- **Current Development**: We are actively working on implementing the Inventory system, Xbox Authentication bypass, and UI Form support to bring full feature parity to ClassiCube players.

## 🦎 Luanti / Minetest Integration: BridgeTest
We extend our crossplay architecture to the open-source voxel world of **Luanti** (formerly Minetest) using **[BridgeTest](https://codeberg.org/l-koehler/bridgetest)**. This integration allows Luanti clients to interact with our Bedrock-based core by handling external protocol translation.

### The Connection Pipeline
To successfully process the movement, chat, and block packets, the traffic must route through the following stack:
1. **Luanti Client**: Connects directly to the **[BridgeTest](https://codeberg.org/l-koehler/bridgetest)** proxy.
2. **BridgeTest**: Handles the heavy translation from Luanti's native protocol.
3. **ViaProxy**: [BridgeTest](https://codeberg.org/l-koehler/bridgetest) **must** be configured to point towards our **ViaProxy** instance.
4. **Nukkit MOT**: ViaProxy delivers the final, clean Bedrock packets to the server core.

### ⚠️ Special Hosting Requirements
Unlike Java-based utilities, **[BridgeTest](https://codeberg.org/l-koehler/bridgetest) is compiled in Rust**. Because of this binary architecture:
- **Environment Constraints**: It will **not** run on standard shared Minecraft hosts that only support `.jar` files.
- **Requirements**: You will need a host with full terminal access (VPS, Dedicated Server, or a custom Pterodactyl egg/Docker container) capable of executing native Rust binaries.

- **Status**: Experimental / Functional. Core movement and text chat are operative, but specialized node/block translations are still under active development.

## 📱 Minecraft Pocket Edition (PE) 0.15.x Integration
We provide two distinct pathways to bridge legacy MCPE 0.15.x clients into the **CreaNexusCore** ecosystem, allowing administrators to choose between stability and native translation depth.

1. **The DragonProxyPocket Path (Java Bridge Experimental)**
  - **Method**: Utilizes an older **GeyserMC** implementation via **DragonProxyPocket**.
  - **Workflow**: DragonProxy routes players through a Java 1.8 interface, which is then proxied into the **Nukkit** MOT core.
  - **Pros/Cons**: This is currently our most stable method for production environments. While it offers superior connectivity, please be aware that the translation layer may occasionally encounter edge-case rendering bugs or entity desyncs due to the protocol conversion depth.
2. **The CraftsMine Path (Native Experimental)**
  - **Method**: Utilizes CraftsMine, a specialized fork of BarrelMC.
  - **Workflow**: This implementation bypasses the Java translation layer, attempting to pipe Pocket 0.15 protocol packets directly into the backend architecture.
  - **Pros/Cons**: Designed as a "closer-to-metal" approach, it offers potentially better performance in specific scenarios. However, it is currently in an early Alpha state. Users should expect frequent console warnings/errors and a significantly higher RAM overhead compared to the DragonProxy method. Recommended for testing and development purposes only.

**💡 Pro-Tip**: If you are hosting a multi-protocol server, avoid using newer, non-standard world formats (such as specific internal Bedrock formats or experimental flat-files) as your primary world directory. This will almost certainly lead to packet translation failures for legacy clients.

.gitmodules de CreaNexusCore:
[submodule "Core"]
	path = Core
	url = https://github.com/MemoriesOfTime/Nukkit-MOT.git
[submodule "McJava"]
	path = McJava
	url = https://github.com/ViaVersion/ViaProxy.git
[submodule "McBeta"]
	path = McBeta
	url = https://github.com/ViaVersionAddons/ViaProxyBeta2Release
[submodule "EaglerCraft"]
	path = EaglerCraft
	url = https://github.com/ayunami2000/ayunViaProxyEagUtils
[submodule "ClassiCube"]
	path = ClassiCube
	url = https://github.com/Creadores-Program/Barrel-CREA-Classic
[submodule "McPocket-0_15_x-java"]
	path = McPocket-0_15_x-java
	url = https://github.com/Creadores-Program/DragonProxyPocket.git
[submodule "McPocket-0_15_x"]
	path = McPocket-0_15_x
	url = https://github.com/Creadores-Program/Craftsmine.git
[submodule "Discord"]
	path = Discord
	url = https://github.com/PetteriM1/DiscordChat.git
[submodule "McLegacy"]
	path = McLegacy
	url = https://github.com/brokiem/voxelbridge.git
[submodule "bridgetest"]
	path = Luanti-Minetest
	url = https://codeberg.org/l-koehler/bridgetest.git