import Test.Tasty (defaultMain, testGroup)
import Test.Tasty.HUnit (assertEqual, testCase)

data Skip = SkipList [Int] [Int] Int Skip | NIL deriving (Eq,Show)


findLastSmallerIndex::Int->[Int]->Int
findLastSmallerIndex val (y:x:xs)
    | y < val && x >= val = 0
    | otherwise = 1 + (findLastSmallerIndex val (x:xs))

findLastSmallerIndex val _ = error "Generic error"

findIndex val (x:xs)
    | x == val = 0
    | otherwise = 1 + (findIndex val (xs))

findIndex _ _ = error "Generic error"

checkValue::[Int]->Int->Int->Int
checkValue array index expected_val
    | (length array) < (index - 1) = error "Value not found"
    | array!!index == expected_val = array!!index
    | otherwise = error "Value not found"

getSkip::Int->Int->Skip->Int
getSkip acessPos val (SkipList array index_array andar skiplist)
    | skiplist /= NIL = getSkip (index_array!!index_before) val skiplist
    | skiplist == NIL = (checkValue array (index_before+1) val)
    | otherwise = error "Generic error"
    where
        index_before = (findLastSmallerIndex val array)


find _ NIL = error "Generic Error"
find val (SkipList array index andar skiplist) = getSkip 0 val (SkipList array index andar skiplist)

insertAt 0 val list = val:list
insertAt pos val (x:xs) = x:(insertAt (pos-1) val xs)

create::Int->Int->Int->Skip
create 0 minVal maxVal = NIL
create size minVal maxVal = (SkipList [minVal,maxVal] [0,1] size (create (size-1) minVal maxVal))

calculateAndar andar = 2 --funcao probabilistica

calculateReferences _ NIL = [0,1]
calculateReferences new_array (SkipList below_array _  _ _) = [(findIndex x below_array) | x<-new_array]


insertSkip::Int->Int->Skip->Skip
insertSkip val andarMax NIL = NIL
insertSkip val andarMax (SkipList array index andar skiplist)
    | andar <= andarMax = (SkipList new_array (calculateReferences new_array skiplist_below) andar skiplist_below)
    | otherwise = (SkipList array (calculateReferences array skiplist_below) andar skiplist_below)
    where
        new_array = insertAt (index_before+1) val array
        skiplist_below = insertSkip val andarMax skiplist
        index_before = (findLastSmallerIndex val array)


insert val insert_andar (SkipList array index andar skiplist) = insertSkip val insert_andar (SkipList array index andar skiplist)
insert _ _ NIL = error "Generic Error"


remove::Int->Skip->Skip
remove _ NIL = NIL
remove val (SkipList array index andar skiplist) = (SkipList new_array (calculateReferences new_array skiplist_below) andar skiplist_below)
    where
        new_array = (filter (/=val) array)
        skiplist_below = remove val skiplist


size NIL = 0
size (SkipList array index andar skiplist)
    | skiplist == NIL = ((length array) - 2)
    | otherwise = size skiplist


height NIL = 0
height (SkipList array index andar skiplist)
        | (length array) > 2 = ((length array) - 2)
        | otherwise = height skiplist

-- Tests

main = defaultMain unitTests

unitTests = testGroup "Skip List tests" [
  emptySkipList,
  sizeAfterInsert,
  sizeAfterRemove,
  searchAfterInsert,
  heightAfterInsertLowElement
  ]

emptySkipList = testCase "Empty Skip List right after creation" $ assertEqual [] 0 (size (create 5 0 100))

sizeAfterInsert = testCase "Size increases by one after insert" $ assertEqual [] 1 (size (insertSkip 5 3 (create 5 0 100)))

sizeAfterRemove = testCase "Size decreases by one after remove" $ assertEqual [] 0 (size (remove 5 (insertSkip 5 3 (create 5 0 100))))

searchAfterInsert = testCase "Returns the inserted element after it being inserted" $ assertEqual [] 5 (find 5 (insertSkip 5 3 (create 5 0 100)))

heightAfterInsertLowElement = testCase "Return the same height after insert a low element" $ assertEqual [] (height (insertSkip 5 3 (create 5 0 100))) (height (insertSkip 6 2 (insert 5 3 (create 5 0 100))))



