"use client";

import { setReady } from "@/actions/participant-ready";
import { Button } from "@/components/ui/button";
import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from "@/components/ui/hover-card";
import { toastProblemDetail } from "@/lib/toast-utils";

type Props = {
  isReady: boolean;
  eligibleToSetReady: boolean;
  challengeId: number;
};

export function ToggleReady({
  isReady,
  eligibleToSetReady,
  challengeId,
}: Props) {
  async function handleClick() {
    const { error } = await setReady(!isReady, challengeId);
    if (error) {
      toastProblemDetail(error);
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
