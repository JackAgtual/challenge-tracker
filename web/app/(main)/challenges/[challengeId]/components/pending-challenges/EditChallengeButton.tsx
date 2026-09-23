import { Button } from "@/components/ui/button";
import Link from "next/link";

type EditChallengeButtonProps = {
  challengeId: number;
};

export default function EditChallengeButton({
  challengeId,
}: EditChallengeButtonProps) {
  return (
    <div>
      <Link href={`/challenges/${challengeId}/edit`}>
        <Button>Edit challenge</Button>
      </Link>
    </div>
  );
}
