import { useDebounce } from "@/hooks/use-debounce";
import { act, renderHook } from "@testing-library/react";

describe("useDebounce", () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  function advance(ms: number) {
    act(() => {
      jest.advanceTimersByTime(ms);
    });
  }

  it("returns the initial value immediately", () => {
    const { result } = renderHook(() => useDebounce("initial", 300));

    expect(result.current[0]).toBe("initial");
  });

  it("does not update the value before the delay has elapsed", () => {
    const { result } = renderHook(() => useDebounce("", 300));

    act(() => result.current[1]("a"));
    advance(299);

    expect(result.current[0]).toBe("");
  });

  it("updates the value once the delay has elapsed", () => {
    const { result } = renderHook(() => useDebounce("", 300));

    act(() => result.current[1]("a"));
    advance(300);

    expect(result.current[0]).toBe("a");
  });

  it("restarts the timer on each change and only emits the last value", () => {
    const { result } = renderHook(() => useDebounce("", 300));

    act(() => result.current[1]("a"));
    advance(200);
    act(() => result.current[1]("ab"));
    advance(200);
    act(() => result.current[1]("abc"));

    // 400ms since the first change, but the timer was reset each time
    expect(result.current[0]).toBe("");

    advance(300);

    expect(result.current[0]).toBe("abc");
  });

  it("supports updater functions like useState", () => {
    const { result } = renderHook(() => useDebounce(1, 300));

    act(() => result.current[1]((prev) => prev + 1));
    advance(300);

    expect(result.current[0]).toBe(2);
  });

  it("clears the pending timer on unmount", () => {
    const { result, unmount } = renderHook(() => useDebounce("", 300));

    act(() => result.current[1]("a"));
    expect(jest.getTimerCount()).toBe(1);

    unmount();

    expect(jest.getTimerCount()).toBe(0);
  });
});
