import unittest
import os
import io
import json
import app

class MainTestCase(unittest.TestCase):
    """Test that the main usecase of reading input and presenting
    output works as intended."""

    def test_paging_mission_control(self):
        dirname = os.path.dirname(__file__)

        ifile = os.path.join(dirname, "data", "input.txt")
        ofile = os.path.join(dirname, "data", "output.txt")

        result = json.load(io.StringIO(app.run(ifile)))

        output = open(ofile)
        expected = json.load(output)
        output.close()

        for left, right in zip(result, expected):
            self.assertDictEqual(left, right)

if __name__ == '__main__':
    unittest.main()