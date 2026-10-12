"use client";

import { setReady } from "@/actions/participant-ready";
import { Button } from "@/components/ui/button";
import { toastGenericError } from "@/lib/toast-utils";
import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from "@/components/ui/hover-card";

type ToggleReadyProps = {
  isReady: boolean;
  eligibleToSetReady: boolean;
  challengeId: number;
};

export default function ToggleReady({
  isReady,
  eligibleToSetReady,
  challengeId,
}: ToggleReadyProps) {
  async function handleClick() {
    const { success } = await setReady(!isReady, challengeId);
    if (!success) {
      toastGenericError();
    }
  }

  const disableButton = !isReady && !eligibleToSetReady;
  const buttonText = isReady ? "Not ready" : "Ready";

  if (!disableButton) {
    return (
      <Button onClick={handleClick} disabled={disableButton}>
        {buttonText}
      </Button>
    );
  }

  return (
    <HoverCard>
      <HoverCardTrigger>
        <Button disabled onClick={handleClick}>
          {buttonText}
        </Button>
      </HoverCardTrigger>
      <HoverCardContent>
        <p>Add at least one goal definition before setting ready.</p>
      </HoverCardContent>
    </HoverCard>
  );
}
