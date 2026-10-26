import { components } from "@/types/api";
import Link from "next/link";
import { User } from "../User";
import { RemoveParticipantButton } from "./RemoveParticipantButton";

type Props = {
  participant: components["schemas"]["ParticipantResponse"];
  challengeId: number;
  challengeIsPending: boolean;
  curUserIsOwner?: boolean;
};

export function Participant({
  participant,
  challengeId,
  challengeIsPending,
  curUserIsOwner,
}: Props) {
  return (
    <div className="flex justify-between py-2 my-0">
      <Link
        className="flex items-center"
        href={`/challenges/${challengeId}/participants/${participant.id}/goals/${challengeIsPending ? "" : "/completions"}`}
      >
        <User user={participant.user} />
      </Link>
      {challengeIsPending && (
        <div className="flex gap-x-2">
          <div className="flex items-center">
            {participant.ready ? "Ready" : "Not ready"}
          </div>
          {curUserIsOwner && !participant.isChallengeOwner && (
            <RemoveParticipantButton
              challengeId={challengeId}
              participantId={participant.id}
            />
          )}
        </div>
      )}
    </div>
  );
}
