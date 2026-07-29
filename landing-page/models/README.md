# 📦 3D Models Folder

Drop your 3D model file here and it will automatically load into the page.

## Supported Formats

| Format | Extension | Notes |
|--------|-----------|-------|
| **glTF Binary** | `.glb` | ✅ **Recommended** — single file, fastest |
| **glTF** | `.gltf` | Needs accompanying `.bin` + texture files |
| **FBX** | `.fbx` | Auto-converted at load |
| **OBJ** | `.obj` | Needs accompanying `.mtl` file |

## File Naming (auto-detected in this order)

The page will look for your file with these names automatically:

1. `models/model.glb` ← **use this name for fastest load**
2. `models/scene.gltf`
3. `models/bottle.glb`
4. `models/laser.glb`

## Quick Start

1. Export your 3D model as **`.glb`** (best format)
2. Rename to `model.glb`
3. Drop it in this folder
4. Refresh the page — it loads automatically

## Recommended Model Specs

- **Polygon count**: < 50,000 triangles (for smooth 60fps)
- **File size**: < 5 MB
- **Textures**: Embedded in `.glb` (or alongside `.gltf`)
- **Origin**: Centered at world origin
- **Scale**: Any — engine auto-scales to fit

## Where to Get 3D Models (free)

- [Sketchfab](https://sketchfab.com) — millions of free models
- [TurboSquid](https://turbosquid.com) — free + premium
- [CGTrader](https://cgtrader.com) — free section
- [Poly Pizza](https://poly.pizza) — low-poly free models
- [Three.js examples](https://threejs.org/examples) — reference models

## What Happens Without a Model?

If no model file is present, the page gracefully falls back to a **procedural 3D model**:

### Hero (mouse-reactive):
- Skincare bottle (lathe geometry, glass material)
- Floats gently + follows mouse

### Scroll-driven section (pinned through 4 stages):
- Glowing crystal (icosahedron, glass material)
- 4 orbiting colored rings (the wavelengths)
- 200 drifting particles
- Color shifts through 4 stages: rose → coral → sage → lilac

Both placeholders match the brand's pink/blush palette.

## File Too Big? Optimize It!

Use [gltf-transform](https://gltf-transform.dev/) to compress:
```bash
npx gltf-transform optimize input.glb output.glb
```

Or use [glb-pipeline](https://github.com/facebookincubator/GLBpipeline):
```bash
npm install -g gltf-pipeline
gltf-pipeline -i model.glb -o model-compressed.glb -d
```

---

*Once you drop your file here, the 3D animation will work automatically — no code changes needed.*
