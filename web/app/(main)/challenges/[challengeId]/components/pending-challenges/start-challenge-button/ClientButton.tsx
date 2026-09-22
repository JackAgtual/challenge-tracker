"use client";

import { startChallenge } from "@/actions/start-challenge";
import { Button } from "@/components/ui/button";
import { toastGenericError } from "@/lib/toast-utils";

type ClientButtonProps = {
  challengeId: number;
  canStart: boolean;
};

export default function ClientButton({
  canStart,
  challengeId,
}: ClientButtonProps) {
  async function handleClick() {
    const { success } = await startChallenge(challengeId);
    if (!success) {
      toastGenericError();
    }
  }

  return (
    <Button disabled={!canStart} onClick={handleClick}>
      Start challenge
    </Button>
  );
}
