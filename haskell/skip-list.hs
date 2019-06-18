data SkipList = SkipList {
  maxHeight :: Int,
  listHead :: Node
}

data Node = Node {
  key :: Int,
  forward :: [Node]
} | Nil

instance Show Node where
  show (Node key _) = show key

new :: Int -> SkipList
new maxHeight =
  SkipList maxHeight listHead
  where
    listHead = Node (minBound :: Int) (replicate maxHeight listTail)
    listTail = Node (maxBound :: Int) (replicate maxHeight Nil)

randomHeight :: Int
randomHeight =
  3

foo :: Node -> Int -> Node
foo node index =
  (forward node) !! index

insertNode :: Int -> Int -> Node -> Bool -> [Node] -> [Node]
insertNode 0 key _ _ pointers =
  (replicate height newNode) ++ (drop height forward)
  where
    height = randomHeight
    forward = zipWith foo pointers [0..]
    newNode = Node key (drop height forward)
insertNode height nkey curNode first pointers
  | nkey > (key forNode) =
    insertNode height nkey forNode True pointers
  | otherwise =
    insertNode (height - 1) nkey curNode False ([curNode] ++ pointers)
  where
    forNode = (forward curNode) !! (height - 1)

insert :: Int -> SkipList -> SkipList
insert key skipList =
  SkipList height (head forward)
  where
    height = maxHeight skipList
    forward = insertNode height key (listHead skipList) True []

main = do
  let skipList = new 5
  print (insertNode (maxHeight skipList) 5 (listHead skipList) [])


