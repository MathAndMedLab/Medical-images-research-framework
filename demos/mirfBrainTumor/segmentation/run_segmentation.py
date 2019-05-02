import os
import sys

if __name__ == '__main__':
    if (len(sys.argv) != 2):
        print('Number of arguments should be 2. e.g.')
        print('    python run_segmentation.py /segmentation/data/brats17/temp')
        exit()
    folder_path = str(sys.argv[1])
    base_command = '. segmentation-env/bin/activate && python3 test.py config/test_all_class.txt '
    os.system(base_command + folder_path)
    exit()
