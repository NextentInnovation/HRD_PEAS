export class Parameters {
    static get scoreNumberFormat() {
        return this._scoreNumberFormat;
    }

    static set scoreNumberFormat( value ) {
        this._scoreNumberFormat = value;
    }
    private static _scoreNumberFormat = '1.1-1';
}
