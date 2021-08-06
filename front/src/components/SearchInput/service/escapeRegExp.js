function escapeRegExp(string) {
  return string && /[\\^$.*+?()[\]{}|]/g.test(string)
    ? string.replace(/[\\^$.*+?()[\]{}|]/g, '\\$&')
    : string || '';
}

export default escapeRegExp;
