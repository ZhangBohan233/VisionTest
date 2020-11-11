import math


def calculate(angle):
    cos = math.cos(angle)
    sin = math.sin(angle)
    left = (8 * cos - 1) / (5 * cos)
    right = cos / sin
    return left - right


def calculate2(angle):
    # cos = math.cos(angle)
    left = 2.5 / (4 - 1 / (2 * math.sin(angle)))
    return left - math.tan(angle)


def binary():
    lower = 0
    upper = 90
    df = 0.0001
    step = 10

    while step > df:
        cur_angle = lower
        min_diff = 10000
        min_angle = cur_angle
        while cur_angle <= upper:
            try:
                diff = calculate2(math.radians(cur_angle))
            except ZeroDivisionError:
                diff = 10000
            if abs(diff) < min_diff:
                min_diff = diff
                min_angle = cur_angle
            cur_angle += step
        lower = min_angle - step
        upper = min_angle + step
        step /= 10

    return (lower + upper) / 2


if __name__ == "__main__":
    print(binary())
