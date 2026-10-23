import { getDisplayNameFromUser } from "@/lib/name-utils";
import { components } from "@/types/api";
import Link from "next/link";
import RemoveParticipantButton from "./RemoveParticipantButton";
import User from "../User";

type ParticipantProps = {
  participant: components["schemas"]["ParticipantResponse"];
  challengeId: number;
  challengeIsPending: boolean;
  curUserIsOwner?: boolean;
};

export default function Participant({
  participant,
  challengeId,
  challengeIsPending,
  curUserIsOwner,
}: ParticipantProps) {
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
