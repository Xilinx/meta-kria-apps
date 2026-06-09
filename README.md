# meta-kria-apps

OpenEmbedded / Yocto layer that packages the Kria SOM application stack
(BIST suite, FOC motor-control server and ROS 2 CANopen client, PMOD
demos, the per-board firmware overlays and the OOT kernel modules they
bind against) into OCI containers consumable on the AMD EDF 2026.1
release. Tested against Yocto Scarthgap.

## Supported recipes and target boards

| Area | Recipe | Notes |
|---|---|---|
| Apps | [kria-bist](recipes-apps/kria-bist) | pytest BIST suite (KD240 / KR260 / KV260) |
| Apps | [foc-motor-ctrl](recipes-apps/foc-motor-ctrl) | FOC lib + py binding + CANopen server + Bokeh dashboard (KD240) |
| Apps | [kria-motor-control](recipes-apps/foc-motor-ctrl) | ROS 2 CANopen client (KR260) |
| Apps | [pmod-rs485-test](recipes-apps/pmod-rs485-test) | RS485 PMOD demo (KR260) |
| Kernel | [motor-control-iio-modules](recipes-kernel/motor-control-iio-modules) | HLS FOC / PWM / QEI / SVPWM / ADC-hub IIO drivers |
| Kernel | [ar1335-module](recipes-kernel/ar1335-module) | AR1335 sensor driver (KV260) |
| Kernel | [mv-camera-sensor-module](recipes-kernel/mv-camera-sensor-module) | FRAMOS IMX547 sensor driver (KR260) |
| Firmware | [kria-bist-fw](recipes-firmware/kria-bist-fw) | BIST overlays (kd240 / kr260 / kv260) |
| Firmware | [kd240-motor-ctrl-qei-fw](recipes-firmware/kd240-motor-ctrl-qei-fw) | KD240 FOC overlay |
| Firmware | [kr260-tsn-rs485pmod-fw](recipes-firmware/kr260-tsn-rs485pmod-fw) | KR260 TSN + RS485 PMOD overlay |
| Firmware | [ap1302-firmware](recipes-firmware/ap1302-firmware) | ON Semi AP1302 ISP blobs (KV260) |
| Containers | [container-kria-bist](recipes-containers/container-kria-bist) | Multi-board BIST runtime |
| Containers | [container-foc-motor-ctrl-server](recipes-containers/container-foc-motor-ctrl) | KD240 FOC server + dashboard |
| Containers | [container-foc-motor-ctrl-ros2-canopen](recipes-containers/container-foc-motor-ctrl) | KR260 ROS 2 CANopen client (runtime) |
| Containers | [container-foc-motor-ctrl-ros2-canopen-dev](recipes-containers/container-foc-motor-ctrl) | KR260 ROS 2 CANopen client (with dev tools) |
| Utils | [dfx-fw-install](recipes-utils/dfx-fw-install) | Container helper: stage fw blobs onto the host fs |
| Utils | [kria-modules-load](recipes-utils/kria-modules-load) | Container helper: modprobe a fw overlay's kernel modules |

## Dependencies

This layer depends on the following Yocto / OpenEmbedded layers
(layer collection name -> source). Branch / SRCREV pins follow the EDF
2026.1 release manifest.

| Collection (`BBFILE_COLLECTIONS`) | URI | Branch |
|---|---|---|
| `core` | https://git.openembedded.org/openembedded-core | scarthgap |
| `openembedded-layer` | https://git.openembedded.org/meta-openembedded (meta-oe) | scarthgap |
| `xilinx-virtualization` | https://github.com/Xilinx/meta-xilinx (meta-xilinx-virtualization) | rel-v2026.1 |
| `kria` | https://github.com/Xilinx/meta-kria | rel-v2026.1 |

The Kernel dependecies for the firmware is explained in [recipes-firmware/module-dependencies.md](recipes-firmware/module-dependencies.md).

## Maintainers / Patches / Submissions

Contributions are accepted via pull request against the `rel-v2026.1`
branch. Please follow the
[OE community commit-message guidelines](https://www.openembedded.org/wiki/Commit_Patch_Message_Guidelines)
for subject formatting, body wrapping, sign-off and trailer style.

**Maintainer:**

    Pallav Joshi <pallav.joshi@amd.com>

## License

This layer is licensed under the MIT License - see [LICENSE](LICENSE)
for the full text. Per-recipe `LICENSE` fields point at the upstream
license of each fetched component; the
[`${COMMON_LICENSE_DIR}/MIT`](https://git.openembedded.org/openembedded-core/tree/meta/files/common-licenses/MIT?h=scarthgap)
file shipped by `openembedded-core` is referenced via
`LIC_FILES_CHKSUM` so recipes packaging arbitrary AMD-authored bits
share a single canonical MIT text.
