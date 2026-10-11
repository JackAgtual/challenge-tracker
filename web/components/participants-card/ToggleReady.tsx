"use client";

import { setReady } from "@/actions/participant-ready";
import { Button } from "@/components/ui/button";
import { toastGenericError } from "@/lib/toast-utils";

type ToggleReadyProps = {
  isReady: boolean;
  challengeId: number;
};

export default function ToggleReady({
  isReady,
  challengeId,
}: ToggleReadyProps) {
  async function handleClick() {
    const { success } = await setReady(!isReady, challengeId);
    if (!success) {
      toastGenericError();
    }
  }

  return (
    <Button onClick={handleClick}>{isReady ? "Not ready" : "Ready"}</Button>
  );
}
