import sys
import random

class Node:

   def __init__(self, value, level):
    self.value = value
    self.forward = (level+1) * [None]
    self.level = level

   def __str__(self):
    return str(self.value)

class SkipList:

  def __init__(self, max_level):
    self.max_level = max_level
    self.head = Node(-sys.maxint - 1, max_level)
    self.tail = Node(sys.maxint, max_level)
    self.size = 0

    def func(level, args):
      self.head.forward[level] = self.tail
    self._common_loop(self.head.level, 0, func, [], False)

  def search(self, value):
    current_max_level = self.head.level
    path = (self.head.level + 1) * [None]
    aux = self.head

    while True:
      path[current_max_level] = (aux, aux.forward[current_max_level])

      # found
      if aux.forward[current_max_level].value == value:
        return [ p for p in path if p != None]

      # skip
      if aux.forward[current_max_level].value < value:
        aux = aux.forward[current_max_level]

      # check level below
      else:
        current_max_level -= 1

      # not in the list
      if current_max_level == -1:
        return [ p for p in path if p != None]

  def add(self, value, level = None):
    if not level:
      level = min(get_level(), self.max_level)

    newNode = Node(value, level)
    path = self.search(value)

    def func(i, args):
      prev = path[i][0]
      nextt = path[i][1]
      prev.forward[i] = newNode
      newNode.forward[i] = nextt

    self._common_loop(level, 0, func, [], False)
    self.size += 1

  def contains(self, value):
    path = self.search(value)
    return path[0][1].value == value


  def remove(self, value):
    if self.contains(value):
      path = self.search(value)
      to_remove = path[0][1]

      def func(level, args):
        prev = args[0][level][0]
        prev.forward[level] = to_remove.forward[level]

      self._common_loop(0, to_remove.level, func, [path], True)

  def height(self):
    return len(filter(lambda node_rep: node_rep[1] != sys.maxint, self.str_rep()))

  def size(self):
    return self.size

  def str_rep(self):
    all_levels = []
    for i in range(self.head.level, -1, -1):

      l = []
      l.append(self.head.value)
      aux = self.head
      while aux.forward[i] != None and aux.forward[i] != self.tail:
        l.append(aux.forward[i].value)
        aux = aux.forward[i]

      if aux.forward[i] == self.tail:
        l.append(self.tail.value)

      all_levels.append(l)

    return all_levels

  def _common_loop(self, level, limit, func, param, asc):
    if asc and level <= limit or not asc and level >= limit:
      func(level, param)
      self._common_loop(level+1, limit, func, param, asc) if asc else self._common_loop(level-1, limit, func, param, asc)


def get_level():
  coin = random.randint(1, 100)
  level = 0

  while coin <= 50:
    level += 1
    coin = random.randint(1, 100)

  return level