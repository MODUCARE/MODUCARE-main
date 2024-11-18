class Stack {
  private storage: {[key: number]: number};
  private size: number;

  constructor() {
    this.storage = {};
    this.size = 0;
  }
  push(item: number) {
    this.size++;
    this.storage[this.size] = item;
  }
  pop() {
    const result = this.storage[this.size];
    delete this.storage[this.size];
    this.size--;
    return result;
  }
  isEmpty() {
    return this.size === 0;
  }
}

export {Stack};
