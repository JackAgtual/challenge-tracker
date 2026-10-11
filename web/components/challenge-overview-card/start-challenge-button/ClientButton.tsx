"use client";

import { startChallenge } from "@/actions/start-challenge";
import { Button } from "@/components/ui/button";
import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from "@/components/ui/hover-card";
import { toastGenericError } from "@/lib/toast-utils";
import { components } from "@/types/api";

type ClientButtonProps = {
  challengeId: number;
  startEligibility: components["schemas"]["ChallengeReadyResponse"];
};

export default function ClientButton({
  challengeId,
  startEligibility,
}: ClientButtonProps) {
  async function handleClick() {
    const { success } = await startChallenge(challengeId);
    if (!success) {
      toastGenericError();
    }
  }

  return (
    <HoverCard>
      <HoverCardTrigger>
        <Button disabled={!startEligibility.ready} onClick={handleClick}>
          Start challenge
        </Button>
      </HoverCardTrigger>
      {!startEligibility.ready && (
        <HoverCardContent>
          <ul className="list-disc list-outside pl-4">
            {startEligibility.reasons?.map((reason) => (
              <li key={reason}>{reason}</li>
            ))}
          </ul>
        </HoverCardContent>
      )}
    </HoverCard>
  );
}
