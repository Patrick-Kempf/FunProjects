from PIL import Image, ImageDraw

def sierpinski(size, steps, foreground=(0, 0, 0), background=(255, 255, 255)):
    image = Image.new('RGB', (size, size), color=background)
    draw = ImageDraw.Draw(image)

    draw.polygon([(0, image.height), (image.width, image.height), (image.width / 2, 0)], fill=foreground)
    if steps <= 0:
        return image

    def draw_triangle(loc, height, offset):
        left = (loc[0] - offset, loc[1] - height)
        right = (loc[0] + offset, loc[1] - height)
        draw.polygon([left, right, loc], fill=background)

    def sierpinski_internal(step, loc, height, hypotenuse):
        offset = (hypotenuse ** 2 - height ** 2) ** 0.5
        if step - 1 <= 0:
            draw_triangle(loc, height, offset)
        else:
            draw_triangle(loc, height, offset)
            sierpinski_internal(step - 1, (loc[0] - offset, loc[1]), height / 2, hypotenuse / 2)  # left
            sierpinski_internal(step - 1, (loc[0] + offset, loc[1]), height / 2, hypotenuse / 2)  # right
            sierpinski_internal(step - 1, (loc[0], loc[1] - height), height / 2, hypotenuse / 2)  # top

    h = (((image.width / 2)**2 + image.height ** 2)**0.5) / 2
    sierpinski_internal(steps, (image.width / 2, image.height), image.height/2, h)

    return image


sierpinski(5000, 10).show()
