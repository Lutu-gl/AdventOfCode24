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
   ,sol <- makeAntinodes (x1, y1, c1) (x2, y2, c2) grid
  ]

isInGrid :: [String] -> (Int, Int) -> Bool
isInGrid grid sol = snd sol < length grid && fst sol < (length $ head grid) && fst sol >= 0 && snd sol >= 0

makeAntinodes :: (Int, Int, Char) -> (Int, Int, Char) -> [String] -> [(Int, Int)]
makeAntinodes (_, _, c1) (_, _, c2) _
    | c1 /= c2 = []
makeAntinodes (x1, y1, c1) (x2, y2, c2) grid =
    [sol | l <- [0 .. (max (length grid) (length (head grid)))], let sol = (x1 + dx*l, y1 + dy*l), isInGrid grid sol]
    where dx = (x1 - x2)
          dy = (y1 - y2)

transformGridWithAntinodes :: [String] -> [(Int, Int)] -> [String]
transformGridWithAntinodes grid [] = grid
transformGridWithAntinodes grid ((x1, y1):xs) =
    transformGridWithAntinodes updatedGrid xs
  where
    updatedRow = updateRow (grid !! y1) x1
    updatedGrid = take y1 grid ++ [updatedRow] ++ drop (y1 + 1) grid

updateRow :: String -> Int -> String
updateRow row x1 = take x1 row ++ "#" ++ drop (x1 + 1) row



main :: IO ()
main = do
  let filePath = "input.txt"
  input <- readInput filePath
  mapM_ putStrLn input
  let antennas = findAntennas input
  print antennas
  let antinodes = calculateAntinodes antennas input
  print (nub antinodes)
  mapM_ putStrLn (transformGridWithAntinodes input antinodes)
  print (length $ nub antinodes)