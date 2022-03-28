export class MultiSelectModel<T> {
    label: string;
    value: T;

    constructor (object: T, label?: string) {
        this.value = object;
        this.label = label ? object[label] : '';
    }

    static listConverter<U>(object: U[], label?: string): MultiSelectModel<U>[] {
        const ret = [];
        object.forEach(value => {
            ret.push( new MultiSelectModel<U>(value, label));
        });
        return ret;
    }
}
