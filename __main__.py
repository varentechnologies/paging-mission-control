import sys
import os.path
import app


def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)


def main():
    if len(sys.argv) < 2:
        eprint("Please include a file as an argument!")
        sys.exit(1)
    
    filename = sys.argv[1]
    if not os.path.isfile(filename):
        eprint("{!r} is not a file!".format(filename))
        sys.exit(1)
    
    print(app.run(filename))
    sys.exit(0)


if __name__ == '__main__':
    main()