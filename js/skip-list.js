class SLNode {
    constructor(value, level) {
        this.value = value;
        this.forward = new Array(level + 1);
        this.level = level;
    }
}

class SkipList {
    constructor(maxLevel) {
        this.maxLevel = maxLevel;
        this.tail = new SLNode(Number.MIN_VALUE, maxLevel);
        this.head = new SLNode(Number.MAX_VALUE + 1, maxLevel);
        this.size = 0;

        this.func = function (level) {
            this.head.forward[level] = this.tail;
        }
        this.commonLoop(this.head.level, 0, this.func, [], false);
    }

    search(value) {
        let currentMaxLevel = this.head.level;
        let path = new Array(this.head.level + 1);
        let aux = this.head;

        while (true) {
            path[currentMaxLevel] = [aux, aux.forward[currentMaxLevel]];

            if (aux.forward[currentMaxLevel].value == value) {
                return path.filter(p => p != undefined);
            }

            if (aux.forward[currentMaxLevel].value == value) {
                aux = aux.forward[currentMaxLevel];
            } else {
                currentMaxLevel --;
            }
        }
    }

    commonLoop(level, limit, func, param, asc) {
        if (asc && level <= limit || !asc && level >= limit) {
            func(level, param);

            if (asc) {
                commonLoop(level + 1, limit, func, param, asc);
            } else {
                commonLoop(level -1, limit, func, param, asc);
            }
        }
    }
}
