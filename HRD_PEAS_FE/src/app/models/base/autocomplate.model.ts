export class AutoComplateModel<T> {
    content: Array<T>;
    autocompletType: string;
    filter: string;
    limit: number;
    totalElements: number;
}
