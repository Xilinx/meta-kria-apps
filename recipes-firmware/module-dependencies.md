# Kria Vitis Platform Firmware: Kernel Module Dependencies

Source-of-truth analysis for which kernel drivers each `.dtbo` overlay
depends on, and which of those drivers are already provided by the AMD
`linux-xlnx` kernel build for `MACHINE=amd-cortexa53-mali-common`
(EDF 2026.1, kernel `6.18.10+git+v2026.1`).

- Overlay sources audited: `kria-vitis-platforms` (public) and
  `kria-vitis-platforms-internal` for KD240, KR260, KV260, K26.
- Kernel `.config` audited: build artefact under
  `build/tmp/work/amd_cortexa53_mali_common-amd-linux/linux-xlnx/6.18.10+git+v2026.1/linux-amd_cortexa53_mali_common-standard-build/.config`.
- Out-of-tree (OOT) recipes referenced live in
  `recipes-kernel/{motor-control-iio-modules,ar1335-module,mv-camera-sensor-module}`.

Legend for the "Kernel status" column:

- `Y` — built into the kernel image (`=y`); always present, no RDEPENDS needed.
- `M` — built as a loadable module (`=m`); the `kernel-module-*` split
  package must be in `RDEPENDS` (or `RRECOMMENDS`) so the rootfs / container
  carries the `.ko`.
- `N` — not enabled in the current `.config`; either the firmware will not
  bind at runtime, or an out-of-tree recipe must provide the module.
- `OOT` — comes from an out-of-tree recipe in this layer.

## Per-firmware dependency tables

### KD240 / `kd240-bist`

Source: `kria-vitis-platforms/kd240/platforms/kd240_bist/dtsi/kd240-bist.dtsi`

| compatible | driver / module | Kernel status | Recipe RDEPENDS |
|---|---|---|---|
| `xlnx,hls-foc-periodic-1.0` | hls-foc-periodic | OOT | `motor-control-iio-modules` |
| `xlnx,hls-pwm-gen-1.0` | hls-pwm-gen | OOT | `motor-control-iio-modules` |
| `xlnx,hls-qei-axi-1.0` | hls-qei-axi | OOT | `motor-control-iio-modules` |
| `xlnx,hls-svpwm-duty-1.0` | hls-svpwm-duty | OOT | `motor-control-iio-modules` |
| `xlnx,adc-hub-1.0` | xilinx-adc-hub | OOT | `motor-control-iio-modules` |
| `generic-uio` | uio_pdrv_genirq | M | `kernel-module-uio-pdrv-genirq` |
| `microchip,mcp25625` | mcp251x | **N** (CONFIG_CAN_MCP251X is not set) | — kernel config gap |
| `xlnx,axi-quad-spi-3.2` | spi-xilinx | Y | none |
| `xlnx,axi-gpio-2.0` / `xlnx,xps-gpio-1.00.a` | gpio-xilinx | Y | none |
| `xlnx,axi-ethernet-7.2` / `xlnx,eth-dma` | xilinx_axienet | Y | none |
| `xlnx,afi-fpga`, `fixed-clock`, `fixed-factor-clock` | core | Y | none |
| `rohm,dh2228fv` (spidev shim) | spidev | Y | none |

### KD240 / `kd240-motor-ctrl-qei`

Source: `kria-vitis-platforms/kd240/platforms/kd240_motor_ctrl_qei/dtsi/kd240-motor-ctrl-qei.dtsi`

| compatible | driver / module | Kernel status | Recipe RDEPENDS |
|---|---|---|---|
| `xlnx,hls-foc-periodic-1.0` | hls-foc-periodic | OOT | `motor-control-iio-modules` |
| `xlnx,hls-pwm-gen-1.0` | hls-pwm-gen | OOT | `motor-control-iio-modules` |
| `xlnx,hls-qei-axi-1.0` | hls-qei-axi | OOT | `motor-control-iio-modules` |
| `xlnx,hls-svpwm-duty-1.0` | hls-svpwm-duty | OOT | `motor-control-iio-modules` |
| `xlnx,adc-hub-1.0` | xilinx-adc-hub | OOT | `motor-control-iio-modules` |
| `xlnx,tsn-endpoint-ethernet-mac-2.0` etc. | xilinx_axienet (TSN build) | Y (CONFIG_XILINX_TSN=y + sub-options) | none |
| `xlnx,xps-intc-1.00.a` | irq-xilinx-intc | Y | none |
| `amd,axi-1wire-host` | amd_axi_w1 | M | `kernel-module-amd-axi-w1` |
| `xlnx,eth-dma` | xilinx_axienet | Y | none |
| `generic-uio` | uio_pdrv_genirq | M | `kernel-module-uio-pdrv-genirq` |
| `xlnx,afi-fpga`, `fixed-factor-clock` | core | Y | none |

### KR260 / `kr260-bist`

Source: `kria-vitis-platforms-internal/kr260/platforms/kr260_bist/dtsi/kr260-bist.dtsi`
(public release tarball ships the same compatibles)

| compatible | driver / module | Kernel status | Recipe RDEPENDS |
|---|---|---|---|
| `framos,imx547` | imx547 | OOT (`mv-camera-sensor-module`) | `mv-camera-sensor-module` |
| `xlnx,axi-iic-2.1` | i2c-xiic | Y (CONFIG_I2C_XILINX=y) | none |
| `xlnx,xxv-ethernet-4.1` | xilinx_axienet (XXV path) | Y (CONFIG_XILINX_AXI_EMAC=y) | none |
| `xlnx,axi-ethernet-7.2` / `xlnx,eth-dma` | xilinx_axienet | Y | none |
| `xlnx,xps-gpio-1.00.a` (commented out today) | gpio-xilinx | Y | none (when re-enabled) |
| `xlnx,axi-frmbuf-wr-v2.2` / `v2.1` | xilinx-frmbuf | Y (CONFIG_XILINX_FRMBUF=y) | none |
| `xlnx,axis-subsetconv-1.1` | xilinx subset converter | Y (CONFIG_VIDEO_XILINX_AXI4S_SUBSETCONV=y) | none |
| `xlnx,video` | xilinx-video pipeline shim | Y (CONFIG_VIDEO_XILINX=y) | none |
| `xlnx,afi-fpga`, `fixed-factor-clock` | core | Y | none |

### KR260 / `kr260-tsn-rs485pmod`

Source: `kria-vitis-platforms/kr260/platforms/kr260_tsn_rs485pmod/dtsi/kr260-tsn-rs485pmod.dtsi`

| compatible | driver / module | Kernel status | Recipe RDEPENDS |
|---|---|---|---|
| `xlnx,tsn-endpoint-ethernet-mac-2.0` etc. | xilinx_axienet (TSN build) | Y | none |
| `xlnx,axi-mcdma-1.1` | xilinx-dma (MCDMA path) | Y (CONFIG_XILINX_DMA=y, AXIENET_HAS_TADMA=y) | none |
| `xlnx,axi-uartlite-rs485` | uartlite | Y (CONFIG_SERIAL_UARTLITE=y) | none |
| `xlnx,axi-quad-spi-3.2` | spi-xilinx | Y | none |
| `microchip,mcp25625` | mcp251x | **N** | — kernel config gap |
| `xlnx,xps-intc-1.00.a` | irq-xilinx-intc | Y (CONFIG_XILINX_INTC=y) | none |
| `generic-uio` | uio_pdrv_genirq | M | `kernel-module-uio-pdrv-genirq` |
| `fixed-clock`, `fixed-factor-clock`, `xlnx,afi-fpga` | core | Y | none |

### KR260 / `kr260-pmod-gps` (recipe not yet authored)

Source: `kria-vitis-platforms/kr260/platforms/kr260_pmod_gps/dtsi/kr260-pmod-gps.dtsi`

| compatible | driver / module | Kernel status | Recipe RDEPENDS |
|---|---|---|---|
| `xlnx,axi-uartlite-2.0` | uartlite | Y | none |
| `pps-gpio` | pps-gpio | **N** (CONFIG_PPS_CLIENT_GPIO is not set; enabled `=m` via this layer's `linux-xlnx` bbappend) | `kernel-module-pps-gpio` |
| `xlnx,afi-fpga`, `fixed-factor-clock` | core | Y | none |

### KV260 / `kv260-bist` (and `kv260-bist-imx708` variant)

Sources: `kria-vitis-platforms/kv260/platforms/kv260_bist/dtsi/kv260-bist.dtsi`
and `.../kv260-bist-imx708.dtsi`.

| compatible | driver / module | Kernel status | Recipe RDEPENDS |
|---|---|---|---|
| `ar1335` | ar1335 | OOT | `ar1335-module` |
| `onnn,ap1302` | ap1302 | M (CONFIG_VIDEO_AP1302=m) | `kernel-module-ap1302` |
| `sony,imx219` | imx219 | Y | none |
| `sony,imx708` (imx708 overlay only) | imx708 | **N** | — driver not in this kernel |
| `nxp,pca9546` | pca954x | Y (CONFIG_I2C_MUX_PCA954x=y) | none |
| `xlnx,axi-iic-2.1` | i2c-xiic | Y | none |
| `xlnx,xps-gpio-1.00.a` | gpio-xilinx | Y | none |
| `xlnx,mipi-csi2-rx-subsystem-5.0` | xilinx-csi2rxss | Y (CONFIG_VIDEO_XILINX_CSI2RXSS=y) | none |
| `xlnx,v-demosaic` | xilinx-demosaic | Y | none |
| `xlnx,v-vpss-scaler-2.2` | xilinx-vpss-scaler | Y | none |
| `xlnx,axi-frmbuf-wr-v2.1` | xilinx-frmbuf | Y | none |
| `xlnx,vcu` | xlnx_vcu | M (CONFIG_XILINX_VCU=m) | `kernel-module-xlnx-vcu` |
| `al,al5e-1.2` / `al,al5d-1.2` | allegro-dvt al5e/al5d encoder/decoder | **N** (CONFIG_VIDEO_ALLEGRO not set) | — userspace VCU pipeline (gst-omx) likely covers; verify |
| `xlnx,video` | xilinx-video shim | Y | none |
| `regulator-fixed`, `fixed-factor-clock`, `xlnx,afi-fpga` | core | Y | none |

### K26 / `k26_base_starter_kit`

No `.dtsi` ships with this platform; uses the kernel's built-in board DT.
No firmware recipe is needed.

## Recipe-level summary

OOT modules to add to `RDEPENDS:${PN}` (correctness-critical: missing module
means the overlay will not bind):

| firmware recipe | RDEPENDS (OOT) |
|---|---|
| `kd240-bist-fw` | `motor-control-iio-modules` |
| `kd240-motor-ctrl-qei-fw` | `motor-control-iio-modules` |
| `kr260-bist-fw` | `mv-camera-sensor-module` |
| `kr260-tsn-rs485pmod-fw` | none (TSN driver is `=y` in kernel) |
| `kr260-pmod-gps-fw` | none |
| `kv260-bist-fw` | `ar1335-module` |

In-tree modules built as `=m` to add via `RDEPENDS:${PN}` (so the
`kernel-module-*` split package is pulled into the rootfs / container feed):

| firmware recipe | kernel modules (`=m`) to RDEPENDS |
|---|---|
| `kd240-bist-fw` | `kernel-module-uio-pdrv-genirq kernel-module-mcp251x` |
| `kd240-motor-ctrl-qei-fw` | `kernel-module-uio-pdrv-genirq kernel-module-amd-axi-w1` |
| `kr260-bist-fw` | none (only the OOT `mv-camera-sensor-module` is needed) |
| `kr260-tsn-rs485pmod-fw` | `kernel-module-uio-pdrv-genirq kernel-module-mcp251x` |
| `kr260-pmod-gps-fw` | `kernel-module-pps-gpio` |
| `kv260-bist-fw` | `kernel-module-ap1302 kernel-module-xlnx-vcu` |

Everything not listed above is built into the kernel image (`=y`) and needs
no recipe-level dependency.

## Kernel config gaps to raise upstream / fix in a kernel bbappend

These would otherwise cause silent runtime failures:

1. `CONFIG_CAN_MCP251X` was `not set` upstream. Required by the
   `microchip,mcp25625` nodes in `kd240-bist` and `kr260-tsn-rs485pmod`.
   Enabled `=m` via this layer's `recipes-kernel/linux/linux-xlnx_%.bbappend`
   + `kria-apps-extra.cfg`; `CONFIG_CAN_VCAN=m` is also enabled there for
   the ROS2 CANopen container's loopback testing.
2. `CONFIG_PPS_CLIENT_GPIO` is `not set` upstream. Required by `pps-gpio` in
   `kr260-pmod-gps`. Enabled `=m` via this layer's
   `recipes-kernel/linux/linux-xlnx_%.bbappend` + `kria-apps-extra.cfg`.
3. `CONFIG_VIDEO_IMX708` is absent. Only the optional
   `kv260-bist-imx708.dtsi` overlay needs it; if that overlay is not
   shipped, no action is required.
4. `CONFIG_VIDEO_ALLEGRO` (or equivalent `al5e`/`al5d` encoder/decoder
   driver) is not built. The KV260 BIST loads the VCU compatible
   (`xlnx,vcu`, served by `xlnx_vcu.ko`); whether the al5e/al5d nodes need a
   matching kernel driver here depends on whether BIST exercises encode /
   decode paths or only verifies the VCU clock controller. Worth confirming
   with the BIST test owner before treating it as a gap.

## Open questions worth resolving before applying these RDEPENDS

1. The TSN driver set is fully `=y` in this kernel
   (`CONFIG_XILINX_TSN=y` plus all sub-options), so the
   `kr260-tsn-rs485pmod-fw` recipe needs no TSN module RDEPENDS. If a
   future kernel reconfig moves these to `=m`, this analysis must be
   re-run.
2. `mv-camera-sensor-module` is built and packaged
   (`kernel-module-imx547`); the KR260 BIST overlay binds the IMX547
   sensor node via this driver.
3. The currently shipped `kr260-bist.dtbo` has the gpio node commented
   out (per the internal repo dts) as a temporary workaround. Once the
   workaround is replaced with the upstream-fixed dtbo that re-introduces
   the `xlnx,xps-gpio-1.00.a` node, the gpio-xilinx driver becomes
   load-bearing (it is `=y` so no RDEPENDS change is needed, but flag in
   the bbappend that the workaround is removable).
4. Allegro al5e/al5d encoder/decoder driver: see config gap #4 above.

## How this analysis was produced

1. For each `.dtsi`, extracted unique `compatible` strings:
   `grep -h '^\s*compatible' <dtsi> | sort -u`.
2. Mapped each compatible to the driver via the in-tree
   `MODULE_DEVICE_TABLE` entries / Kconfig symbols.
3. Queried the kernel `.config` for each symbol's `=y` / `=m` / not-set
   state.
4. Cross-checked that every `=m` entry actually produced a `.ko` under
   `linux-amd_cortexa53_mali_common-standard-build/`.

Re-run this analysis whenever `linux-xlnx` is rebased or a defconfig
fragment changes module/builtin status.
