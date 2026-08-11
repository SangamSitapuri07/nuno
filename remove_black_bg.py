import os
try:
    from PIL import Image
except ImportError:
    print("Installing Pillow library...")
    os.system("python -m pip install Pillow")
    from PIL import Image

dir_path = os.path.join("android", "app", "src", "main", "res", "drawable")
files = [
    "ic_play_tile_3d.png",
    "ic_treasure_chest_3d.png",
    "ic_card_pedestal_3d.png",
    "avatar_sangam.png",
    "avatar_trexy.png"
]

for name in files:
    path = os.path.join(dir_path, name)
    if os.path.exists(path):
        img = Image.open(path).convert("RGBA")
        datas = img.getdata()
        newData = []
        for item in datas:
            # If pixel is dark black/gray (RGB < 35), turn it 100% transparent
            if item[0] < 35 and item[1] < 35 and item[2] < 35:
                newData.append((0, 0, 0, 0))
            else:
                newData.append(item)
        img.putdata(newData)
        img.save(path, "PNG")
        print(f"Successfully converted {name} to 100% Transparent PNG!")

print("\nFinished! Rebuild your Android project in Android Studio to see the transparent images.")