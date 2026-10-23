import { Dispatch, SetStateAction, useEffect, useState } from "react";

export function useDebounce<T>(
  initialValue: T,
  delayMs = 250
): [T, Dispatch<SetStateAction<T>>] {
  const [realtime, setRealtime] = useState(initialValue);
  const [debounce, setDebounce] = useState(initialValue);
  const [timeoutId, setTimeoutId] = useState<NodeJS.Timeout>();

  useEffect(() => {
    clearTimeout(timeoutId);

    const curTimeoutId = setTimeout(() => {
      setDebounce(realtime);
    }, delayMs);

    setTimeoutId(curTimeoutId);

    return () => clearTimeout(curTimeoutId);
  }, [realtime]);

  return [debounce, setRealtime];
}
