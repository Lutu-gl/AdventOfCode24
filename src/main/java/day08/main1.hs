import System.IO (readFile)
import Data.List (nub)

readInput :: FilePath -> IO [String]
readInput filePath = do
  content <- readFile filePath
  return (lines content)

findAntennas :: [String] -> [(Int, Int, Char)]
findAntennas [] = []
findAntennas grid = [(x, y, c) | (row, y) <- zip grid [0..], (c, x) <- zip row [0..], c /= '.']

calculateAntinodes :: [(Int, Int, Char)] -> [String] -> [(Int, Int)]
calculateAntinodes antennas grid =
  [ sol | (x1, y1, c1) <- antennas, (x2, y2, c2) <- antennas, c1 == c2, (x1, y1) /= (x2, y2)
   ,let maybeSol = makeAntinode (x1, y1, c1) (x2, y2, c2), Just sol <- [maybeSol] -- Filters `Nothing` and handles `Just`
   , isInGrid grid sol
  ]

isInGrid :: [String] -> (Int, Int) -> Bool
isInGrid grid sol = snd sol < length grid && fst sol < (length $ head grid) && fst sol >= 0 && snd sol >= 0

makeAntinode :: (Int, Int, Char) -> (Int, Int, Char) -> Maybe (Int, Int)
makeAntinode (_, _, c1) (_, _, c2)
    | c1 /= c2 = Nothing
makeAntinode (x1, y1, c1) (x2, y2, c2) = Just (x1 + (x1 - x2), y1 + (y1 - y2))


main :: IO ()
main = do
  let filePath = "input.txt"
  input <- readInput filePath
  mapM_ putStrLn input
  let antennas = findAntennas input
  print antennas
  let antinodes = calculateAntinodes antennas input
  print (nub antinodes)
  print (length $ nub antinodes)
