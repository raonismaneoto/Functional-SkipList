let skipList;

function create(height) {
  console.log(height);
  skipList = new SkipList(height);
}

function hasCreated() {
  return skipList !== undefined;
}

function addToSl(value) {
  skipList.add(value);
}

function search(value) {
  skipList.search(value);
}

function remove(value) {
  skipList.remove(value);
}

function height() {
  alert(skipList.height());
}

function size() {
  alert(skipList.size());
}

function showSl() {
  alert(skipList.strRep());
}