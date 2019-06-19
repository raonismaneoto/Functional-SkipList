const getLevel = () => {
  let coin,
    level = 0
  do {
    ++level
    coin = Math.floor(Math.random() * 100) + 1
  } while (coin <= 50)
  return level
}

class Node {
  constructor(value, level) {
    this.value = value
    this.level = level
    this.forward = new Array(level + 1).map(() => undefined)
  }

  toString() {
    return this.value
  }
}

export default class SkipList {
  constructor(maxLevel) {
    this.size = 0
    this.maxLevel = maxLevel
    this.head = new Node(-Infinity, maxLevel)
    this.tail = new Node(+Infinity, maxLevel)

    const func = (level, args) => {
      this.head.forward[level] = this.tail
    }

    this.traversal(this.head.level, 0, func, [], false)
  }

  traversal(level, limit, func, param, asc) {
    if ((asc && level <= limit) || (!asc && level >= limit)) {
      func(level, param)
      if (asc) {
        this.traversal(level + 1, limit, func, param, asc)
      } else {
        this.traversal(level - 1, limit, func, param, asc)
      }
    }
  }

  search(value) {
    let curMaxLevel = this.head.level
    let path = Array(curMaxLevel + 1).map(() => undefined)
    let aux = this.head

    const func = (acc, node) => {
      if (node !== undefined) {
        return [...acc, node]
      }
      return acc
    }

    while (true) {
      path[curMaxLevel] = [aux, aux.forward[curMaxLevel]]
      if (aux.forward[curMaxLevel].value === value) {
        return path.reduce(func, [])
      }
      if (aux.forward[curMaxLevel].value < value) {
        aux = aux.forward[curMaxLevel]
      } else {
        --curMaxLevel
      }
      if (curMaxLevel < 0) {
        return path.reduce(func, [])
      }
    }
  }

  insert(value, level = undefined) {
    if (!level) {
      level = Math.min(getLevel(), this.maxLevel)
    }
    let newNode = new Node(value, level)
    let path = this.search(value)

    const func = (level, args) => {
      let prev = path[level][0]
      let next = path[level][1]
      prev.forward[level] = newNode
      newNode.forward[level] = next
    }

    this.traversal(level, 0, func, [], false)
    ++this.size
  }

  contains(value) {
    const path = this.search(value)
    return value === path[0][1].value
  }

  remove(value) {
    if (this.contains(value)) {
      let path = this.search(value)
      const toRemove = path[0][1]

      const func = (level, args) => {
        let prev = args[0][level][0]
        prev.forward[level] = toRemove.forward[level]
      }

      this.traversal(0, toRemove.level, func, [path], true)
    }
  }

  getSize() {
    return this.size
  }

  getHeight() {
    return this.maxLevel
  }

  toList() {
    let allLevels = []

    const func = (level, args) => {
      let curLevel = []
      curLevel.push(this.head.toString())
      let aux = this.head
      while (
        aux.forward[level] !== undefined &&
        aux.forward[level] !== this.tail
      ) {
        aux = aux.forward[level]
        curLevel.push(aux.toString())
      }
      curLevel.push(this.tail.toString())
      allLevels.push(curLevel)
    }

    this.traversal(this.head.level, 0, func, [], false)
    return allLevels
  }
}
