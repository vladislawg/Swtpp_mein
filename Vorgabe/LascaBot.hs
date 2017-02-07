--- module (NICHT AENDERN!)
module LascaBot where
--- imports (NICHT AENDERN!)
import Util
import Data.Char
import System.Environment
--- external signatures (NICHT AENDERN!)
getMove   :: String -> String
listMoves :: String -> String

-- *==========================================* --
-- |    HIER BEGINNT EURE IMPLEMENTIERUNG!    | --
-- *==========================================* --

--- types/structures (TODO)

data Color = White | Black
data Stone = Stone { color :: Color, isOfficer :: Bool }
data Position = Position { stones :: [Stone] }
data BoardRow = BoardRow { positions :: [Position]}
data Board = Board { rows :: [BoardRow] }
data GameState = GameState { activeColor :: Color, board :: Board}

instance Show Stone where
    show (Stone White False) = "w"
    show (Stone White True) = "W"
    show (Stone Black False) = "b"
    show (Stone Black True) = "B"

instance Show Position where
    show (Position []) = ""
    show (Position (element:list)) = (show element) ++ (show (Position list))

instance Show BoardRow where
    show (BoardRow []) = ""
    show (BoardRow (element:[])) = show element
    show (BoardRow (element:list)) = (show element) ++ "," ++ (show (BoardRow list))

instance Show Board where
    show (Board []) = ""
    show (Board (element:[])) = show element
    show (Board (element:list)) = (show element) ++ "/" ++ (show (Board list))

instance Show GameState where
    show (GameState color board) = (show board) ++ " " ++ (show color)
    --- ... ---

--- logic (TODO)
getMove   s = "g3-f4" -- Eigene Definition einfügen!
listMoves s = "[g3-f4,a3-c5]" -- Eigene Definition einfügen!

    --- ... ---

--- input (TODO)
parseInput :: [String] -> GameState
parseInput (boardString:colorString:[]) = GameState (parseColor colorString) (parseBoard boardString)
--parseInput (board:color:[])   = ... (parseColor color) ...
--parseInput (board:color:move:[]) = ... (parseColor color) ...

parseBoard :: String -> Board
parseBoard s = Board (parseBoardRows (splitOn "/" s))

parseBoardRows :: [String] -> [BoardRow]
parseBoardRows [] = []
parseBoardRows (row:list) = [(parseBoardRow (splitOn "," row))] ++ (parseBoardRows list)

parseBoardRow :: [String] -> BoardRow
parseBoardRow list = (BoardRow (parsePositions list))

parsePositions :: [String] -> [Position]
parsePositions [] = []
parsePositions (element:list) = [(parsePosition element)] ++ (parsePositions list)

parsePosition :: String -> Position
parsePosition s = (Position (parseStones s))

parseStones :: [Char] -> [Stone]
parseStones [] = []
parseStones (c:list) = [(parseStone c)] ++ (parseStones list)

parseStone :: Char -> Stone
parseStone 'w' = Stone White False
parseStone 'W' = Stone White True
parseStone 'b' = Stone Black False
parseStone 'B' = Stone Black True

parseColor :: String -> Color
parseColor "w" = White
parseColor "b" = Black

toInt :: Char -> Int
toInt c = ((ord c) - (ord 'a') + 1)

--getPosition :: Board -> [Char] -> Position
--getPosition board pos = getPositionFromList (positions board) pos

--getPositionFromList :: [Position] -> [Char] -> Position
--getPositionFromList (element:[]) _ = element -- has to be the correct choice when the input is valid
--getPositionFromList (element:list) pos = if pos == (coordinates element) then element else (getPositionFromList list pos)

    --- ... ---

--- output (TODO)

colorToString :: Color -> String
colorToString White = "w"
colorToString Black = "b"

instance Show Color where
    show = colorToString

instance Eq Color where
    (==) White White = True
    (==) Black Black = True
    (==) _ _ = False

    --- ... ---
