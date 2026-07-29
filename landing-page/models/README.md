# 📦 3D Models — Currently Using: `laser_hair_removal_device.glb`

## ✅ Active Model

The page is currently configured to load:

**`models/laser_hair_removal_device.glb`** (12.16 MB) — your uploaded laser hair removal device model

## How It Works

The 3D engine automatically:

1. **Loads `laser_hair_removal_device.glb`** on page load with Draco/Meshopt support
2. **Shows a progress indicator** with percentage while downloading
3. **Centers and auto-scales** the model to fit the hero (mouse-reactive) and scroll section (pinned)
4. **Falls back to a procedural laser device** if your model fails to load
5. **Optimizes materials** (boosts envMapIntensity, adjusts roughness) for premium look

## 3D Animation Features

### Hero (mouse-reactive)
- Gentle auto-rotation
- Follows mouse cursor (X/Y axis tilt)
- Floating sine-wave animation
- Pulse scale animation
- Fades out as user scrolls past hero

### Scroll-driven section (pinned 4 stages)
The model is **pinned** as user scrolls through 4 stages:

| Stage | Behavior |
|-------|----------|
| **1. The Beauty** | Model rotates, glow effect, pink palette |
| **2. The Tech** | 4 orbiting wavelength rings appear (the colors match your model's brand) |
| **3. The Result** | Camera zooms in, model spins 180° |
| **4. The Promise** | Camera dollies back, full reveal, particles intensify |

Background gradient **shifts colors** as you scroll through stages (cream → pink → rose → deep rose).

## Performance

The model is **12.16 MB** which is large but acceptable for premium landing pages. The engine:
- Uses **DRACO compression** if your model is compressed (saves ~70% size)
- Uses **Meshopt decoder** for further optimization
- Uses **progressive loading** with real-time progress bar
- **Reduces quality on mobile** automatically

## Optimize Your Model (Optional, Recommended)

To reduce the 12MB file size:

```bash
# Install gltf-transform (one-time)
npm install -g @gltf-transform/cli

# Compress
gltf-transform optimize input.glb output.glb --texture-compress webp --texture-size 2048
```

Or use the online tool: https://gltf-transform.dev/

## File Structure

```
landing-page/
├── index.html
├── models/
│   ├── laser_hair_removal_device.glb  ← YOUR MODEL (12.16 MB)
│   └── README.md
└── ...
```

## Troubleshooting

**Model not loading?**
- Check browser console (F12) for errors
- Verify file is at exactly: `landing-page/models/laser_hair_removal_device.glb`
- Check file isn't corrupted (re-upload to GitHub)
- Fallback procedural model will show automatically

**Model too big/too small?**
- Edit the auto-scale value in `index.html`:
  - Hero: `const targetSize = isMobile ? 2.5 : 2.8;` 
  - Scroll: `userModel.scale.setScalar(4.5 / size);`

**Model in wrong position?**
- Edit the offset in `index.html`:
  - Hero: `if (!isMobile) model.position.x = 1.6;`

---

*This is your real, uploaded model — drop in replacements anytime and they'll auto-load.*
