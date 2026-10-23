"use client";

import { startChallenge } from "@/actions/start-challenge";
import { Button } from "@/components/ui/button";
import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from "@/components/ui/hover-card";
import { toastProblemDetail } from "@/lib/toast-utils";
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
    const { error } = await startChallenge(challengeId);
    if (error) {
      toastProblemDetail(error);
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
