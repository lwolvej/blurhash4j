## blurhash4j

### Ablout

This project is a blurhash encoder and decoder implementation in pure Java.

### How to use it

```
//encoder
BufferedImage image = ImageIO.read(new File("test.jpg"));
String blurhashStr = BlurHash.encode(image); //LGF5]+Yk^6#M@-5c,1J5@[or[Q6.

//decoder
int[] data = BlurHash.decode("LGF5]+Yk^6#M@-5c,1J5@[or[Q6.", width, height, 1);
BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
image.setRGB(0, 0, width, height, data, 0, width);
ImageIO.write(image, "jpg", new File("filename.jpg"));
```

### Licence

This project is licensed under the [MIT License](LICENSE).